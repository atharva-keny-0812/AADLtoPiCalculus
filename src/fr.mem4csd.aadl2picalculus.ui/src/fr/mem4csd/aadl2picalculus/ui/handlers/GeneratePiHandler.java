package fr.mem4csd.aadl2picalculus.ui.handlers;

import java.io.File;
import java.util.ArrayList;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.handlers.HandlerUtil;

import fr.mem4csd.aadl2picalculus.acceleo.pi.main.Generate; 
import fr.mem4csd.aadl2picalculus.ui.wizards.ExportToPiWizard;

public class GeneratePiHandler extends AbstractHandler {

    @Override
    public Object execute(ExecutionEvent event) {
        IStructuredSelection selection = (IStructuredSelection) HandlerUtil.getCurrentSelection(event);
        IFile aadlFile = (IFile) selection.getFirstElement();

        if (aadlFile == null) return null;

        String commandId = event.getCommand().getId();

        // 1. IF CLICKED FROM OSATE MENU -> Open the Wizard
        if (commandId.equals("fr.mem4csd.aadl2picalculus.ui.exportCustom")) {
            ExportToPiWizard wizard = new ExportToPiWizard(aadlFile);
            WizardDialog dialog = new WizardDialog(HandlerUtil.getActiveShell(event), wizard);
            dialog.open();
        } 
        // 2. IF RIGHT-CLICKED -> Original Quick Task
        else {
            try {
                URI modelURI = URI.createPlatformResourceURI(aadlFile.getFullPath().toString(), true);
                File targetFolder = new File(aadlFile.getParent().getLocation().toOSString());

                Generate generator = new Generate(modelURI, targetFolder, new ArrayList<String>());
                generator.doGenerate(null);
                
                aadlFile.getParent().refreshLocal(1, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}