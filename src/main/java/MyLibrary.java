import dao.BookDao;
import dao.BookDaoSQLite;
import model.Book;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class MyLibrary extends JFrame {

    private BookDao bookDao;
    private JPanel addingPanel = new JPanel();
    private JPanel searchPanel = new JPanel();
    private JPanel resultPanel = new JPanel();
    private JTextField titleField = new JTextField(12);
    private JTextField authorField = new JTextField(12);
    private JTextField findField = new JTextField(30);
    private DefaultListModel listModel = new DefaultListModel();
    private JList resultList = new JList(listModel);
    private JScrollPane scrollResultsArea = new JScrollPane(resultList);

    public MyLibrary() {
        super("MyLibrary");
        setBookDao(new BookDaoSQLite());
        int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
        int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
        int frameHeight = 430;
        int frameWidth = 400;
        this.setBounds((screenWidth - frameWidth) / 2, (screenHeight - frameHeight) / 2, frameWidth, frameHeight);
        this.setResizable(false);
        initComponents();
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public BookDao getBookDao() {
        return bookDao;
    }

    public void setBookDao(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    public void createAddingPanel() {
        addingPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED), "Add book"));
        addingPanel.add(new JLabel("Title"));
        addingPanel.add(titleField);
        addingPanel.add(new JLabel("Author"));
        addingPanel.add(authorField);
        createButton("Add", addingPanel, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String tmpTitle = titleField.getText();
                String tmpAuthor = authorField.getText();
                if (!(tmpTitle.equals("") && tmpAuthor.equals("")))
                    getBookDao().addBook(new Book(tmpTitle, tmpAuthor));
                titleField.setText("");
                authorField.setText("");
            }
        });
        this.getContentPane().add(addingPanel);
    }

    public void createSearchPanel() {
        searchPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED), "Search by"));
        searchPanel.add(findField);
        createButton("Title", searchPanel, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                listModel.removeAllElements();
                String tmpTitle = findField.getText();
                List<Book> bookList = getBookDao().findBookByTitle(tmpTitle);
                for (int i = 0; i < bookList.size(); i++) {
                    listModel.addElement(bookList.get(i));
                }
            }
        });
        createButton("Author", searchPanel, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                listModel.removeAllElements();
                String tmpAuthor = findField.getText();
                List<Book> bookList = getBookDao().findBookByAuthor(tmpAuthor);
                for (int i = 0; i < bookList.size(); i++) {
                    listModel.addElement(bookList.get(i));
                }
            }
        });
        this.getContentPane().add(searchPanel);
    }

    public void createResultsPanel() {
        resultPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED), "Search results"));
        scrollResultsArea.setPreferredSize(new Dimension(330, 70));
        resultPanel.add(scrollResultsArea);
        createButton("Delete", resultPanel, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int option = JOptionPane.showConfirmDialog(rootPane, "Are you sure?", "Deleting book", JOptionPane.YES_NO_CANCEL_OPTION);
                if (option == 0) {
                    int[] tab = resultList.getSelectedIndices();
                    Book tmpBook;
                    for (int i = 0; i < tab.length; i++) {
                        tmpBook = (Book) listModel.getElementAt(tab[i]);
                        getBookDao().deleteBook(tmpBook);
                        listModel.remove(tab[i]);
                    }
                }
            }
        });

        createButton("Clear", resultPanel, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                listModel.removeAllElements();
            }
        });

        createButton("Show all", resultPanel, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                listModel.removeAllElements();
                List<Book> bookList = getBookDao().findAllBooks();
                for (int i = 0; i < bookList.size(); i++) {
                    listModel.addElement(bookList.get(i));
                }
            }
        });
        this.getContentPane().add(resultPanel);
    }

    public void initComponents() {
        this.getContentPane().setLayout(new GridLayout(3, 1));
        createAddingPanel();
        createSearchPanel();
        createResultsPanel();
    }

    public void createButton(String name, JPanel panel, ActionListener actionListener) {
        JButton button = new JButton(name);
        button.addActionListener(actionListener);
        panel.add(button);
    }

    public static void main(String[] args) {
        new MyLibrary().setVisible(true);
    }
}
