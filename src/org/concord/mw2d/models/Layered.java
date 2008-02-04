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

package org.concord.mw2d.models;

import java.awt.Graphics;
import java.awt.Point;

public interface Layered {

	public final static int FRONT = 1;
	public final static int BACK = 2;

	public void setLayer(int i);

	public int getLayer();

	public void setHost(ModelComponent mc);

	public ModelComponent getHost();

	public Point getCenter();

	public void setLocation(double x, double y);

	public void translateBy(double dx, double dy);

	public boolean contains(double x, double y);

	public int getWidth();

	public int getHeight();

	public void paint(Graphics g);

}