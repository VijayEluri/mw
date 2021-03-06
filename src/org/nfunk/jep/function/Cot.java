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

/** added by Connie Chen to JEP
 *   Concord Consortium 8/2/2003
 */

package org.nfunk.jep.function;

import java.util.Stack;

import org.nfunk.jep.ParseException;
import org.nfunk.jep.type.Complex;

public class Cot extends PostfixMathCommand {

	public Cot() {
		numberOfParameters = 1;
	}

	public String toString() {
		return "The cotangent function";
	}

	public void run(Stack inStack) throws ParseException {
		checkStack(inStack);// check the stack
		Object param = inStack.pop();
		inStack.push(cot(param));// push the result on the inStack
	}

	public Object cot(Object param) throws ParseException {
		if (param instanceof Number) {
			double v = ((Number) param).doubleValue();
			return new Double(1.0 / Math.tan(v));
		}
		else if (param instanceof Complex) {
			return ((Complex) param).cot();
		}
		throw new ParseException("Invalid parameter type");
	}

}
