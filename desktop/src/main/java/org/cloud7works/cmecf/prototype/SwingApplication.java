
package org.cloud7works.cmecf.prototype;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

import org.cloud7works.backend.ejb.CustomerFacadeRemote;
import org.cloud7works.domain.model.Customer;

/**
 * @author cloud7works
 *
 */
public class SwingApplication extends JFrame {

    CustomerForm form;
    JLabel countLabel = new JLabel();
    JLabel UScourtsLabel = new JLabel();
    JButton newCustomer = new JButton("Add new");
  
    
    String[] columnNames = new String[]{"Case Number", "Filer", "Filer Email"};
    private JTable table;

    private List<Customer> customers;

    private CustomerFacadeRemote customerFacade;

    public static void main(String args[]) throws IOException {
        new SwingApplication().createUI();
        
    }

    void deselect() {
        table.getSelectionModel().clearSelection();
    }

    class CustomerTableModel extends AbstractTableModel {

        @Override
        public int getRowCount() {
            return customers.size();
        }

        @Override
        public int getColumnCount() {
            return 3;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            if (customers == null) {
                customers = customerFacade.findAll();
            }
            Customer c = customers.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return c.getCaseNumber();
                case 1:
                    return c.getFiler();
                case 2:
                    return c.getFilerEmail();
            }
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public String getColumnName(int column) {
            return columnNames[column];
        }

    }

    private void createUI() throws IOException {
        final BorderLayout borderLayout = new BorderLayout(10, 10);
        setLayout(borderLayout);

        newCustomer.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                form.clear();
            }
        });

        form = new CustomerForm(this);
        
//        Class clazz = SwingApplication.class;
//        InputStream inputStream = clazz.getResourceAsStream("uscourts.png");
//        BufferedImage myPicture = ImageIO.read(inputStream);
////        JLabel picLabel = new JLabel(new ImageIcon(myPicture));
////        picLabel.setSize(1,1);
//        
//        ImageIcon imageIcon = new ImageIcon(new ImageIcon(myPicture).getImage().getScaledInstance(120, 60, Image.SCALE_DEFAULT));
//        JLabel picLabel = new JLabel(imageIcon);
        
        UScourtsLabel.setText("Appellate Legacy Application Assessment CM/ECF");

        Box hbox = Box.createHorizontalBox();   
        hbox.add(Box.createHorizontalStrut(50)); 
        hbox.add(UScourtsLabel);
        hbox.add(Box.createHorizontalStrut(100)); 
        hbox.add(newCustomer);
        hbox.add(Box.createGlue());
        hbox.add(countLabel);
        hbox.add(Box.createHorizontalStrut(100)); 
        add(hbox, BorderLayout.PAGE_START);

        table = new JTable();
        table.getSelectionModel().setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(
                new ListSelectionListener() {

                    @Override
                    public void valueChanged(ListSelectionEvent e) {
                        Customer c = customers.get(e.getFirstIndex());
                        form.editCustomer(c);
                    }
                });
        add(new JScrollPane(table), BorderLayout.CENTER);

        add(form, BorderLayout.PAGE_END);

        refreshData();

        setSize(640, 400);

        setVisible(true);
    }

    protected void refreshData() {
        customers = getCustomerFacade().findAll();
        // Manual style, almost like IndexexCotainer or custom Container 
        // in vaadin, see impl.
        table.setModel(new CustomerTableModel());
        countLabel.setText("Cases in DB: " + customers.size());
    }

    public CustomerFacadeRemote getCustomerFacade() {
        if (customerFacade == null) {
            try {
                final Object ref = new InitialContext(
                        getJndiPropsCustomerServer()).lookup(
                                "CustomerFacadeRemote");
                customerFacade = (CustomerFacadeRemote) PortableRemoteObject.
                        narrow(ref, CustomerFacadeRemote.class);
            } catch (NamingException ex) {
                throw new RuntimeException(ex);
            }
        }
        return customerFacade;
    }

    protected Properties getJndiPropsCustomerServer() {
        Properties props = new Properties();
        props.put(Context.INITIAL_CONTEXT_FACTORY,
                "org.apache.openejb.client.RemoteInitialContextFactory");
        props.put(Context.PROVIDER_URL, "http://127.0.0.1:8080/tomee/ejb");
        return props;
    }
}
