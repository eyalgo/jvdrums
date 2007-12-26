/*
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package ui.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import jvdrums.Info;
import ui.swing.StandardDialog;

/**
 * Panel that displays information about jOrgan.
 */
@SuppressWarnings("serial")
public class AboutPanel extends JPanel {

	/**
	 * Create an about panel.
	 */
	public AboutPanel() {
		setLayout(new BorderLayout());
		add(new JLabel(createIcon()));
	}

	private ImageIcon createIcon() {
        final ResourceBundle imgBundle = ResourceBundle.getBundle("ui.utils.ImageBundle");
        final ImageIcon icon = (ImageIcon) imgBundle.getObject("about.png");
		int width = icon.getIconWidth();
		int height = icon.getIconHeight();
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) image.getGraphics();
		g.drawImage(icon.getImage(), 0, 0, this);
		addVersion(g, width, height);
		g.dispose();
		icon.setImage(image);
		return icon;
	}

	private void addVersion(Graphics2D g, int width, int height) {
		String version = new Info().getVersion();
		g.setFont(new Font("Lucida", Font.PLAIN, 11));//Sans Serif
		g.setColor(Color.ORANGE);
		g.drawString(version, 65, 15);
	}

	/**
	 * Utility method to show an about panel in a dialog.
	 * 
	 * @param owner
	 *            owning frame
	 */
	public static void showInDialog(JFrame owner) {
		AboutPanel aboutPanel = new AboutPanel();
		StandardDialog dialog = new StandardDialog(owner);
		dialog.setBody(aboutPanel);
		dialog.setResizable(false);
		dialog.setBounds(null);
        dialog.setTitle("About JVDrums");
		dialog.setVisible(true);
		dialog.dispose();
	}
}