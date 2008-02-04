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

import java.util.Map;

import javax.swing.JDialog;
import javax.swing.SwingUtilities;

import org.concord.modeler.text.Page;

/**
 * @author Charles Xie
 * 
 */
abstract class ComponentMaker {

	private final static String EXECUTE_MW_SCRIPT = "Execute MW script";
	private final static String EXECUTE_JMOL_SCRIPT = "Execute Jmol script";

	boolean cancel;

	static boolean isScriptActionKey(String s) {
		return EXECUTE_MW_SCRIPT.equals(s) || EXECUTE_JMOL_SCRIPT.equals(s) || "Script".equals(s);
	}

	static Object getScriptAction(Map m) {
		Object o = m.get(EXECUTE_MW_SCRIPT);
		if (o != null)
			return o;
		o = m.get(EXECUTE_JMOL_SCRIPT);
		if (o != null)
			return o;
		return m.get("Script");
	}

	static boolean needNewDialog(JDialog dialog, Page page) {
		if (dialog == null)
			return true;
		if (dialog.getOwner() != SwingUtilities.getWindowAncestor(page))
			return true;
		return false;
	}

	abstract void invoke(Page page);

}