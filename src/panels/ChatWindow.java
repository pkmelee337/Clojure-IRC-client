package panels;
import javax.swing.JPanel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JList;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;


public class ChatWindow extends JPanel {
	private final JList list = new JList();
	private JTextField textField;

	/**
	 * Create the panel.
	 */
	public ChatWindow() {
		list.setBorder(new LineBorder(new Color(0, 0, 0)));
		
		JTextArea txtrTest = new JTextArea();
		
		textField = new JTextField();
		textField.setColumns(10);
		
		JButton btnSend = new JButton("Send");
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
					.addComponent(list, GroupLayout.PREFERRED_SIZE, 160, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(txtrTest, GroupLayout.PREFERRED_SIZE, 572, GroupLayout.PREFERRED_SIZE)
						.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
							.addComponent(textField)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnSend)))
					.addContainerGap(17, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
					.addComponent(list, GroupLayout.DEFAULT_SIZE, 511, Short.MAX_VALUE)
					.addGroup(groupLayout.createSequentialGroup()
						.addComponent(txtrTest, GroupLayout.PREFERRED_SIZE, 451, GroupLayout.PREFERRED_SIZE)
						.addGap(10)
						.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
							.addComponent(textField, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
							.addComponent(btnSend))
						.addContainerGap()))
		);
		setLayout(groupLayout);
		
	}
}
