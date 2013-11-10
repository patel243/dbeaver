/*
 * Copyright (C) 2010-2013 Serge Rieder
 * serge@jkiss.org
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package org.jkiss.dbeaver.ui.actions.navigator;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jkiss.dbeaver.DBException;
import org.jkiss.dbeaver.core.DBeaverCore;
import org.jkiss.dbeaver.model.DBUtils;
import org.jkiss.dbeaver.model.navigator.*;
import org.jkiss.dbeaver.runtime.VoidProgressMonitor;
import org.jkiss.dbeaver.ui.UIUtils;
import org.jkiss.dbeaver.ui.dialogs.EnterNameDialog;
import org.jkiss.utils.CommonUtils;

public class NavigatorHandlerLocalFolderCreate extends AbstractHandler {

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException
    {
        final ISelection selection = HandlerUtil.getCurrentSelection(event);

        if (selection instanceof IStructuredSelection) {
            IStructuredSelection structSelection = (IStructuredSelection) selection;
            Object element = structSelection.getFirstElement();
            if (element instanceof DBNDataSource) {
                createFolder(HandlerUtil.getActiveWorkbenchWindow(event), (DBNDataSource) element, null);
            }
        }
        return null;
    }

    public static boolean createFolder(IWorkbenchWindow workbenchWindow, final DBNDataSource node, String newName)
    {
        if (newName == null) {
            newName = EnterNameDialog.chooseName(workbenchWindow.getShell(), "Folder name");
        }
        if (CommonUtils.isEmpty(newName) || newName.equals(node.getNodeName())) {
            return false;
        }
        node.getDataSourceContainer().setFolderPath(newName);

        DBNNode parentNode = node.getParentNode();
        if (parentNode instanceof DBNProjectDatabases) {
            ((DBNProjectDatabases)parentNode).refreshChildren();
        }

        return true;
    }
}
