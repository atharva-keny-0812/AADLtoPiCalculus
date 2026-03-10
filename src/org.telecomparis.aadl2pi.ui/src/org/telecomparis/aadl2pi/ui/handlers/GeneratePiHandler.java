package org.telecomparis.aadl2pi.ui.handlers;

import java.io.File;
import java.util.ArrayList;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

// Use the exact package and class from your screenshot
import org.telecomparis.acceleo.pi.main.Generate; 

public class GeneratePiHandler extends AbstractHandler {

    @Override
    public Object execute(ExecutionEvent event) {
        // Get the selected AADL file
        IStructuredSelection selection = (IStructuredSelection) HandlerUtil.getCurrentSelection(event);
        IFile aadlFile = (IFile) selection.getFirstElement();

        if (aadlFile != null) {
            URI modelURI = URI.createPlatformResourceURI(aadlFile.getFullPath().toString(), true);
            File targetFolder = new File(aadlFile.getParent().getLocation().toOSString());

            try {
                // Call the 'Generate' class from your exported JAR
                Generate generator = new Generate(modelURI, targetFolder, new ArrayList<String>());
                generator.doGenerate(null);
                
                // Refresh to show the new Pi-calculus file
                aadlFile.getParent().refreshLocal(1, null);
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}