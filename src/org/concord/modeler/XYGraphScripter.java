/*
 *   Copyright (C) 2007  The Concord Consortium, Inc.,
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

import java.awt.EventQueue;

/**
 * @author Charles Xie
 * 
 */
class XYGraphScripter extends ComponentScripter {

	private PageXYGraph graph;

	XYGraphScripter(PageXYGraph graph) {
		super(true);
		this.graph = graph;
		setName("XY graph script runner #" + graph.getIndex());
	}

	protected void evalCommand(String ci) {
		if ("snapshot".equalsIgnoreCase(ci)) {
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					SnapshotGallery.sharedInstance().takeSnapshot(graph.getPage().getAddress(), graph.getGraph());
				}
			});
			return;
		}
	}

}
