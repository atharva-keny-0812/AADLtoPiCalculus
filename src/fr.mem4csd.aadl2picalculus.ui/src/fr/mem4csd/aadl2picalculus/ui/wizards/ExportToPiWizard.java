package fr.mem4csd.aadl2picalculus.ui.wizards;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.util.URI;
import org.eclipse.swt.widgets.DirectoryDialog;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import fr.mem4csd.aadl2picalculus.acceleo.pi.main.Generate;

public class ExportToPiWizard extends Wizard {
    private ComponentSelectionPage selectionPage;
    private IFile selectedAadlFile;

    public ExportToPiWizard(IFile file) {
        this.selectedAadlFile = file;
        setWindowTitle("Export to Pi-Calculus");
    }

    @Override
    public void addPages() {
        // Placeholder data - you can replace this with actual AADL parsing later
        List<String> components = Arrays.asList("System_Impl", "Process_A", "Thread_1"); 
        selectionPage = new ComponentSelectionPage(components);
        addPage(selectionPage);
    }

    @Override
    public boolean performFinish() {
        List<String> selected = selectionPage.getSelectedComponents();
        
        DirectoryDialog folderDlg = new DirectoryDialog(getShell());
        folderDlg.setText("Select Destination Folder");
        folderDlg.setMessage("Select where you want to save the generated .pi files on your computer.");
        String path = folderDlg.open();

        if (path != null && !selected.isEmpty()) {
            try {
                // Convert IFile to a file-system URI for Acceleo
                URI modelURI = URI.createFileURI(selectedAadlFile.getLocation().toOSString());
                File targetFolder = new File(path);
                
                // Pass selected components as arguments to the Acceleo Generate class
                ArrayList<String> args = new ArrayList<>(selected);

                Generate generator = new Generate(modelURI, targetFolder, args);
                generator.doGenerate(null);
                
                return true;
            } catch (Exception e) { 
                e.printStackTrace(); 
            }
        }
        return false;
    }
}