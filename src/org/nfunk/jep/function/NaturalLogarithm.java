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

/*****************************************************************************

 JEP - Java Math Expression Parser 2.24
 December 30 2002
 (c) Copyright 2002, Nathan Funk
 See LICENSE.txt for license information.

 *****************************************************************************/
package org.nfunk.jep.function;

import java.util.Stack;

import org.nfunk.jep.ParseException;
import org.nfunk.jep.type.Complex;

public class NaturalLogarithm extends PostfixMathCommand {

	public NaturalLogarithm() {
		numberOfParameters = 1;
	}

	public String toString() {
		return "The natural logarithm";
	}

	public void run(Stack inStack) throws ParseException {
		checkStack(inStack);// check the stack
		Object param = inStack.pop();
		inStack.push(ln(param));// push the result on the inStack
	}

	public Object ln(Object param) throws ParseException {
		if (param instanceof Number) {
			// TODO: think about only returning Complex if param is <0
			// Complex temp = new Complex(((Number)param).doubleValue());
			// return temp.log();
			return new Double(Math.log(((Number) param).doubleValue())); // modified by Connie Chen

		}
		else if (param instanceof Complex) {
			return ((Complex) param).log();
		}
		throw new ParseException("Invalid parameter type");
	}
}
