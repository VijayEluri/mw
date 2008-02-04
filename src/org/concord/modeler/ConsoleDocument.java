/*
 *   Copyright (C) 2006  The Concord Consortium, Inc.,
 *   25 Love Lane, Concord, MA 01742
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program; if not, write to the Free Software
 *   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * END LICENSE */

package org.concord.modeler;

import java.awt.Color;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Position;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

class ConsoleDocument extends DefaultStyledDocument {

	private ConsoleTextPane consoleTextPane;

	private SimpleAttributeSet attError;
	private SimpleAttributeSet attEcho;
	private SimpleAttributeSet attPrompt;
	private SimpleAttributeSet attUserInput;
	private SimpleAttributeSet attStatus;

	private Position positionBeforePrompt; // starts at 0, so first time isn't tracked (at least on Mac OS X)
	private Position positionAfterPrompt; // immediately after $, so this will track
	private int offsetAfterPrompt; // only still needed for the insertString override and replaceCommand

	ConsoleDocument() {

		super();

		attError = new SimpleAttributeSet();
		StyleConstants.setForeground(attError, Color.red);

		attPrompt = new SimpleAttributeSet();
		StyleConstants.setForeground(attPrompt, Color.magenta);

		attUserInput = new SimpleAttributeSet();
		StyleConstants.setForeground(attUserInput, Color.black);

		attEcho = new SimpleAttributeSet();
		StyleConstants.setForeground(attEcho, Color.blue);
		StyleConstants.setBold(attEcho, true);

		attStatus = new SimpleAttributeSet();
		StyleConstants.setForeground(attStatus, Color.black);
		StyleConstants.setItalic(attStatus, true);

	}

	void setConsoleTextPane(ConsoleTextPane consoleTextPane) {
		this.consoleTextPane = consoleTextPane;
	}

	/** Removes all content of the script window, and add a new prompt. */
	void clearContent() {
		try {
			super.remove(0, getLength());
		}
		catch (javax.swing.text.BadLocationException exception) {
			System.out.println("Could not clear script window content: " + exception.getMessage());
		}
		setPrompt();
	}

	void setPrompt() {
		try {
			super.insertString(getLength(), "$ ", attPrompt);
			offsetAfterPrompt = getLength();
			positionBeforePrompt = createPosition(offsetAfterPrompt - 2);
			// after prompt should be immediately after $ otherwise tracks the end
			// of the line (and no command will be found) at least on Mac OS X it did.
			positionAfterPrompt = createPosition(offsetAfterPrompt - 1);
			consoleTextPane.setCaretPosition(offsetAfterPrompt);
		}
		catch (BadLocationException e) {
		}
	}

	/**
	 * it looks like the positionBeforePrompt does not track when it started out as 0 and a insertString at location 0
	 * occurs. It may be better to track the position after the prompt in stead
	 */
	void outputBeforePrompt(String str, SimpleAttributeSet attribute) {
		try {
			Position caretPosition = createPosition(consoleTextPane.getCaretPosition());
			super.insertString(positionBeforePrompt.getOffset(), str + "\n", attribute);
			// keep the offsetAfterPrompt in sync
			offsetAfterPrompt = positionBeforePrompt.getOffset() + 2;
			consoleTextPane.setCaretPosition(caretPosition.getOffset());
		}
		catch (BadLocationException e) {
			e.printStackTrace(System.err);
		}
	}

	void outputError(String strError) {
		outputBeforePrompt(strError, attError);
	}

	void outputErrorForeground(String strError) {
		try {
			super.insertString(getLength(), strError + "\n", attError);
			consoleTextPane.setCaretPosition(getLength());
		}
		catch (BadLocationException e) {
		}
	}

	void outputEcho(String strEcho) {
		outputBeforePrompt(strEcho, attEcho);
	}

	void outputStatus(String strStatus) {
		outputBeforePrompt(strStatus, attStatus);
	}

	void appendNewline() {
		try {
			super.insertString(getLength(), "\n", attUserInput);
			consoleTextPane.setCaretPosition(getLength());
		}
		catch (BadLocationException e) {
		}
	}

	/**
	 * override the insertString to make sure everything typed ends up at the end or in the 'command line' using the
	 * proper font, and the newline is processed.
	 */
	public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
		// System.out.println("insertString("+offs+","+str+",attr)");
		int ichNewline = str.indexOf('\n');
		if (ichNewline > 0)
			str = str.substring(0, ichNewline);
		if (ichNewline != 0) {
			if (offs < offsetAfterPrompt) {
				offs = getLength();
			}
			super.insertString(offs, str, attUserInput);
			consoleTextPane.setCaretPosition(offs + str.length());
		}
		if (ichNewline >= 0) {
			consoleTextPane.enterPressed();
		}
	}

	String getCommandString() {
		String strCommand = "";
		try {
			int cmdStart = positionAfterPrompt.getOffset();
			// skip unnecessary leading spaces in the command.
			strCommand = getText(cmdStart, getLength() - cmdStart).trim();
		}
		catch (BadLocationException e) {
		}
		return strCommand;
	}

	public void remove(int offs, int len) throws BadLocationException {
		// System.out.println("remove("+offs+","+len+")");
		if (offs < offsetAfterPrompt) {
			len -= offsetAfterPrompt - offs;
			if (len <= 0)
				return;
			offs = offsetAfterPrompt;
		}
		super.remove(offs, len);
		// consoleTextPane.setCaretPosition(offs);
	}

	public void replace(int offs, int length, String str, AttributeSet attrs) throws BadLocationException {
		// System.out.println("replace("+offs+","+length+","+str+",attr)");
		if (offs < offsetAfterPrompt) {
			if (offs + length < offsetAfterPrompt) {
				offs = getLength();
				length = 0;
			}
			else {
				length -= offsetAfterPrompt - offs;
				offs = offsetAfterPrompt;
			}
		}
		super.replace(offs, length, str, attUserInput);
		// consoleTextPane.setCaretPosition(offs + str.length());
	}

	/**
	 * Replaces current command on script.
	 * 
	 * @param newCommand
	 *            new command value
	 * @throws BadLocationException
	 */
	void replaceCommand(String newCommand) throws BadLocationException {
		replace(offsetAfterPrompt, getLength() - offsetAfterPrompt, newCommand, attUserInput);
	}

}