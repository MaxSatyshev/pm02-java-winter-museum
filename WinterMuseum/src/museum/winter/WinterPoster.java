package museum.winter;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public final class WinterPoster extends JPanel {
	private static final Color PAPER = new Color(248, 246, 239);
	private static final Color INK = new Color(24, 39, 59);
	private static final Color GOLD = new Color(235, 194, 108);
	private static final Color SKY = new Color(19, 45, 72);
	private static final Dimension DESIGN_SIZE = new Dimension(1080, 655);

	private final SnowField snow = new SnowField(54, 2206);
	private final Scene scene = new Scene();
	private final Timer timer = new Timer(33, event -> tick());
	private boolean paused;
	private long previousNanos;

	public WinterPoster() {
		super(new BorderLayout(0, 0));
		setBackground(PAPER);
		setPreferredSize(DESIGN_SIZE);
		add(header(), BorderLayout.NORTH);
		add(scene, BorderLayout.CENTER);
		add(details(), BorderLayout.EAST);
	}

	private JPanel header() {
		JPanel bar = new JPanel(new BorderLayout(13, 0));
		bar.setBackground(new Color(17, 34, 55));
		bar.setBorder(BorderFactory.createEmptyBorder(17, 28, 17, 25));
		bar.add(new JLabel(new ImageIcon(mark())), BorderLayout.WEST);
		JLabel heading = new JLabel("<html><b>МУЗЕЙ МУЗЫКИ</b><br>Учебная зимняя афиша</html>");
		heading.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		heading.setForeground(new Color(244, 238, 216));
		bar.add(heading, BorderLayout.CENTER);
		JButton pause = new JButton("Пауза");
		pause.addActionListener(event -> {
			setPaused(!paused);
			pause.setText(paused ? "Продолжить" : "Пауза");
		});
		JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 8));
		actions.setOpaque(false);
		actions.add(pause);
		bar.add(actions, BorderLayout.EAST);
		return bar;
	}

	private JPanel details() {
		JPanel details = new JPanel();
		details.setPreferredSize(new Dimension(325, 0));
		details.setLayout(new BoxLayout(details, BoxLayout.Y_AXIS));
		details.setBackground(PAPER);
		details.setBorder(BorderFactory.createEmptyBorder(28, 23, 22, 23));
		details.add(label("Коллекция", 27, true));
		details.add(Box.createVerticalStrut(6));
		details.add(label("Темы для знакомства с музеем", 14, false));
		details.add(Box.createVerticalStrut(18));
		details.add(exhibit("01  ·  Струнные", "Скрипки и история скрипичных мастеров."));
		details.add(Box.createVerticalStrut(12));
		details.add(exhibit("02  ·  Клавишные", "Инструменты и механика клавишного звука."));
		details.add(Box.createVerticalStrut(12));
		details.add(exhibit("03  ·  Звук и запись", "От живого исполнения к звукозаписи."));
		return details;
	}

	private static JPanel exhibit(String heading, String description) {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBackground(Color.WHITE);
		panel.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(new Color(223, 218, 204)),
			BorderFactory.createEmptyBorder(13, 14, 15, 14)));
		panel.setMaximumSize(new Dimension(300, 99));
		panel.setAlignmentX(LEFT_ALIGNMENT);
		panel.add(label(heading, 16, true));
		panel.add(Box.createVerticalStrut(5));
		panel.add(label("<html>" + description + "</html>", 13, false));
		return panel;
	}

	private static JLabel label(String text, int size, boolean bold) {
		JLabel label = new JLabel(text);
		label.setFont(new Font("Segoe UI", bold ? Font.BOLD : Font.PLAIN, size));
		label.setForeground(INK);
		label.setAlignmentX(LEFT_ALIGNMENT);
		return label;
	}

	private void setPaused(boolean value) {
		paused = value;
		snow.setPaused(value);
		if (paused) {
			timer.stop();
		} else {
			previousNanos = System.nanoTime();
			timer.start();
		}
	}

	private void tick() {
		long now = System.nanoTime();
		double elapsed = (now - previousNanos) / 1_000_000_000.0;
		previousNanos = now;
		snow.step(Math.min(elapsed, 0.10));
		scene.repaint();
	}

	private static Graphics2D smooth(Graphics2D g) {
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		return g;
	}

	private static BufferedImage mark() {
		BufferedImage image = new BufferedImage(48, 48, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = smooth(image.createGraphics());
		g.setColor(GOLD);
		g.setStroke(new BasicStroke(2.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g.drawOval(3, 3, 42, 42);
		g.drawArc(12, 14, 24, 22, 184, 172);
		g.drawLine(15, 23, 33, 23);
		g.drawLine(21, 18, 21, 29);
		g.drawLine(27, 18, 27, 29);
		g.drawLine(19, 36, 29, 36);
		g.dispose();
		return image;
	}

	private static BufferedImage snowSprite() {
		BufferedImage image = new BufferedImage(24, 24, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = smooth(image.createGraphics());
		g.setColor(new Color(233, 246, 255, 180));
		g.setStroke(new BasicStroke(1.1f));
		for (int i = 0; i < 3; i++) {
			double angle = i * Math.PI / 3;
			int dx = (int) Math.round(10 * Math.cos(angle));
			int dy = (int) Math.round(10 * Math.sin(angle));
			g.drawLine(12 - dx, 12 - dy, 12 + dx, 12 + dy);
		}
		g.dispose();
		return image;
	}

	private final class Scene extends JPanel {
		private final BufferedImage sprite = snowSprite();

		@Override protected void paintComponent(Graphics graphics) {
			super.paintComponent(graphics);
			Graphics2D g = smooth((Graphics2D) graphics.create());
			int width = getWidth();
			int height = getHeight();
			if (width == 0 || height == 0) {
				g.dispose();
				return;
			}
			g.setPaint(new GradientPaint(0, 0, SKY, 0, height, new Color(83, 129, 153)));
			g.fillRect(0, 0, width, height);
			drawLandscape(g, width, height);
			for (SnowField.Flake flake : snow.snapshot()) {
				int x = (int) (flake.x() * width);
				int y = (int) (flake.y() * height);
				g.drawImage(sprite, x, y, flake.size(), flake.size(), null);
			}
			g.setColor(new Color(15, 36, 57, 220));
			g.fillRoundRect(27, 27, 327, 117, 14, 14);
			g.setFont(new Font("Georgia", Font.BOLD, 33));
			g.setColor(new Color(255, 244, 214));
			g.drawString("Зимняя афиша", 48, 75);
			g.setFont(new Font("Segoe UI", Font.PLAIN, 15));
			g.drawString("Музыкальные инструменты и их истории", 49, 109);
			g.dispose();
		}

		private void drawLandscape(Graphics2D g, int w, int h) {
			g.setColor(new Color(244, 225, 179));
			g.fillOval(w * 76 / 100, h * 13 / 100, 91, 91);
			Path2D slope = new Path2D.Double();
			slope.moveTo(0, h * 0.84);
			slope.curveTo(w * 0.35, h * 0.60, w * 0.55, h * 0.93, w, h * 0.69);
			slope.lineTo(w, h);
			slope.lineTo(0, h);
			slope.closePath();
			g.setColor(new Color(205, 222, 230));
			g.fill(slope);
			int x = w / 3;
			int y = h * 53 / 100;
			int body = w * 42 / 100;
			g.setColor(new Color(33, 60, 88));
			g.fillRect(x, y, body, h * 28 / 100);
			Path2D roof = new Path2D.Double();
			roof.moveTo(x - 16, y);
			roof.lineTo(x + body / 2.0, y - h * 0.15);
			roof.lineTo(x + body + 16, y);
			roof.closePath();
			g.setColor(new Color(28, 49, 73));
			g.fill(roof);
			g.setColor(GOLD);
			for (int i = 0; i < 5; i++) {
				int windowX = x + (i + 1) * body / 6 - 12;
				g.fillRoundRect(windowX, y + 23, 24, 39, 10, 10);
			}
			g.setColor(new Color(11, 30, 49));
			g.fillRoundRect(x + body / 2 - 21, y + 84, 42, h * 28 / 100 - 84, 18, 18);
			g.setColor(new Color(241, 247, 247));
			g.fillArc(-w / 8, h * 80 / 100, w + w / 4, h / 2, 0, 180);
		}
	}

	private static void layoutAll(java.awt.Container parent) {
		parent.doLayout();
		for (java.awt.Component child : parent.getComponents()) {
			if (child instanceof java.awt.Container inner) layoutAll(inner);
		}
	}

	private void saveFrame(Path path) throws IOException {
		setSize(DESIGN_SIZE);
		layoutAll(this);
		BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics2D g = image.createGraphics();
		printAll(g);
		g.dispose();
		ImageIO.write(image, "png", path.toFile());
	}

	public static void main(String[] args) {
		if (args.length == 2 && "--capture".equals(args[0])) {
			Path folder = Path.of(args[1]);
			SwingUtilities.invokeLater(() -> {
				try {
					Files.createDirectories(folder);
					WinterPoster poster = new WinterPoster();
					poster.saveFrame(folder.resolve("frame-a.png"));
					poster.snow.step(1.25);
					poster.saveFrame(folder.resolve("frame-b.png"));
					poster = new WinterPoster();
					for (int i = 0; i < 32; i++) {
						poster.saveFrame(folder.resolve("motion-%02d.png".formatted(i)));
						poster.snow.step(0.10);
					}
				} catch (IOException ex) {
					throw new java.io.UncheckedIOException(ex);
				}
			});
			return;
		}

		SwingUtilities.invokeLater(() -> {
			JFrame frame = new JFrame("Музей музыки · Зимняя афиша");
			WinterPoster poster = new WinterPoster();
			frame.setContentPane(poster);
			frame.setIconImage(mark());
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.pack();
			frame.setMinimumSize(new Dimension(825, 560));
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);
			poster.setPaused(false);
		});
	}
}
