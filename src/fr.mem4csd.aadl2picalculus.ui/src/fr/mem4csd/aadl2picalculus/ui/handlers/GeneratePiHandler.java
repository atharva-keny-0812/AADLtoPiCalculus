package fr.mem4csd.aadl2picalculus.ui.handlers;

import java.io.File;
import java.util.ArrayList;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.statushandlers.StatusManager;

import fr.mem4csd.aadl2picalculus.acceleo.pi.main.Generate;
import fr.mem4csd.aadl2picalculus.ui.wizards.ExportToPiWizard;

public class GeneratePiHandler extends AbstractHandler {

    @Override
    public Object execute(ExecutionEvent event) {
        IStructuredSelection selection = (IStructuredSelection) HandlerUtil.getCurrentSelection(event);
        IFile aadlFile = (IFile) selection.getFirstElement();

        if (aadlFile == null) {
            MessageDialog.openWarning(HandlerUtil.getActiveShell(event), 
                "No Selection", "Please select an AADL instance file (.aaxl2).");
            return null;
        }

        String commandId = event.getCommand().getId();

        if (commandId.equals("fr.mem4csd.aadl2picalculus.ui.exportCustom")) {
            ExportToPiWizard wizard = new ExportToPiWizard(aadlFile);
            WizardDialog dialog = new WizardDialog(HandlerUtil.getActiveShell(event), wizard);
            dialog.open();
            return null;
        }

        try {
            File targetFolder = new File(aadlFile.getParent().getLocation().toOSString());
            
            // Record timestamp before generation (to detect updated file later)
            long beforeGen = System.currentTimeMillis();
            
            // Run generation
            URI modelURI = URI.createPlatformResourceURI(aadlFile.getFullPath().toString(), true);
            Generate generator = new Generate(modelURI, targetFolder, new ArrayList<String>());
            generator.doGenerate(null);
            
            aadlFile.getParent().refreshLocal(1, null);
            
            // Find the .pi file most recently modified (after generation started)
            File[] piFiles = targetFolder.listFiles((dir, name) -> name.toLowerCase().endsWith(".pi"));
            File generatedFile = null;
            if (piFiles != null) {
                for (File f : piFiles) {
                    if (f.lastModified() >= beforeGen) {
                        if (generatedFile == null || f.lastModified() > generatedFile.lastModified()) {
                            generatedFile = f;
                        }
                    }
                }
            }
            
            // Report result
            if (generatedFile != null && generatedFile.exists()) {
                MessageDialog.openInformation(HandlerUtil.getActiveShell(event), 
                    "Pi‑calculus Generation", 
                    "File generated (or updated):\n" + generatedFile.getAbsolutePath());
            } else {
                MessageDialog.openWarning(HandlerUtil.getActiveShell(event), 
                    "Generation Completed", 
                    "No .pi file was found after generation.\n\n" +
                    "Possible reasons:\n" +
                    "- The instance file does not contain a valid SystemInstance.\n" +
                    "- Generation produced no output.\n" +
                    "Check the Error Log for details.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Status status = new Status(IStatus.ERROR, "fr.mem4csd.aadl2picalculus.ui", 
                "Generation failed: " + e.getMessage(), e);
            StatusManager.getManager().handle(status, StatusManager.SHOW);
            MessageDialog.openError(HandlerUtil.getActiveShell(event), 
                "Pi‑calculus Generation Error", 
                "An error occurred.\n\n" + e.toString() + 
                "\n\nSee Error Log for details.");
        }
        return null;
    }
}