package org.telecomparis.aadl2pi.ui.parts;

import jakarta.annotation.PostConstruct;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.common.util.BasicMonitor;
import org.telecomparis.acceleo.pi.main.Generate; 

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;

public class SampleView {
    private Text pathText;
    private Text piDisplay;

    @PostConstruct
    public void createPartControl(Composite parent) {
        // Main layout setup: 3 columns (Label, Text, Browse Button)
        parent.setLayout(new GridLayout(3, false));

        new Label(parent, SWT.NONE).setText("AADL Instance:");

        pathText = new Text(parent, SWT.BORDER);
        pathText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        pathText.setEditable(false);

        Button browseBtn = new Button(parent, SWT.PUSH);
        browseBtn.setText("Browse...");
        browseBtn.addListener(SWT.Selection, e -> {
            FileDialog dialog = new FileDialog(parent.getShell(), SWT.OPEN);
            dialog.setFilterExtensions(new String[] {"*.aaxl2", "*.aadl"}); 
            String path = dialog.open();
            if (path != null) {
                pathText.setText(path);
            }
        });

        // Container for Action Buttons (Convert and Clear)
        Composite buttonContainer = new Composite(parent, SWT.NONE);
        buttonContainer.setLayout(new GridLayout(2, true));
        buttonContainer.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));

        // 1. Convert Button
        Button runBtn = new Button(buttonContainer, SWT.PUSH);
        runBtn.setText("Convert & Preview");
        runBtn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        runBtn.addListener(SWT.Selection, e -> {
            executeTransformation();
        });

        // 2. Clear Button
        Button clearBtn = new Button(buttonContainer, SWT.PUSH);
        clearBtn.setText("Clear All");
        clearBtn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        clearBtn.addListener(SWT.Selection, e -> {
            pathText.setText("");  
            piDisplay.setText(""); 
        });

        new Label(parent, SWT.NONE).setText("Pi-Calculus Preview:");
        piDisplay = new Text(parent, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
        GridData textData = new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1);
        textData.heightHint = 400; // Increased height for better visibility
        piDisplay.setLayoutData(textData);
        piDisplay.setEditable(false);
    }

    /**
     * Handles the AADL to Pi-Calculus transformation logic.
     */
    private void executeTransformation() {
        String inputPath = pathText.getText();
        if (inputPath.isEmpty()) {
            piDisplay.setText("Error: No AADL file selected.");
            return;
        }

        try {
            File inputFile = new File(inputPath);
            // Define the output folder relative to the input file
            File targetFolder = new File(inputFile.getParent(), "output-pi");

            // --- REFRESH LOGIC: Clear old generated files first ---
            if (targetFolder.exists()) {
                File[] existingFiles = targetFolder.listFiles();
                if (existingFiles != null) {
                    for (File f : existingFiles) {
                        f.delete(); 
                    }
                }
            } else {
                targetFolder.mkdirs();
            }

            // Prepare URI for Acceleo
            URI modelURI = URI.createFileURI(inputPath);
            
            // Initialize the Acceleo Generator
            // Ensure Generate class is properly imported from your project
            Generate generator = new Generate(modelURI, targetFolder, new ArrayList<Object>());
            
            // Execute the generation
            generator.doGenerate(new BasicMonitor()); 
            
            // --- FILE RETRIEVAL LOGIC ---
            // Look for the resulting .pi or .txt files
            File[] files = targetFolder.listFiles((dir, name) -> 
                name.toLowerCase().endsWith(".pi") || name.toLowerCase().endsWith(".txt")
            );
            
            if (files != null && files.length > 0) {
                // Sort by last modified to ensure the absolute latest file is picked up
                Arrays.sort(files, (a, b) -> Long.compare(b.lastModified(), a.lastModified()));
                
                // Read and display the content
                String content = new String(Files.readAllBytes(files[0].toPath()));
                piDisplay.setText(content); 
            } else {
                piDisplay.setText("Success: Transformation completed, but no output files were detected in: " 
                                  + targetFolder.getAbsolutePath());
            }
            
        } catch (Exception ex) {
            piDisplay.setText("Critical Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @Focus
    public void setFocus() {
        pathText.setFocus();
    }
}