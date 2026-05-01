package fr.mem4csd.aadl2picalculus.ui.wizards;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import java.util.ArrayList;
import java.util.List;

public class ComponentSelectionPage extends WizardPage {
    private TableViewer tableViewer;
    private List<String> componentNames;

    protected ComponentSelectionPage(List<String> componentNames) {
        super("SelectionPage");
        setTitle("Select Components");
        this.componentNames = componentNames;
    }

    @Override
    public void createControl(Composite parent) {
        // Create a standard Table with the CHECK style bit
        Table table = new Table(parent, SWT.CHECK | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
        tableViewer = new TableViewer(table);
        
        tableViewer.setContentProvider(ArrayContentProvider.getInstance());
        tableViewer.setInput(componentNames);
        
        setControl(table);
    }

    public List<String> getSelectedComponents() {
        List<String> selected = new ArrayList<>();
        // Manually check which items are ticked in the SWT Table
        for (TableItem item : tableViewer.getTable().getItems()) {
            if (item.getChecked()) {
                selected.add(item.getText());
            }
        }
        return selected;
    }
}