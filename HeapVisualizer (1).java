import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class HeapVisualizer extends JFrame {
    ArrayList<Integer> heap = new ArrayList<>();
    JTextField inputField;
    JButton insertButton, resetButton, randomButton;
    JLabel swapLabel;
    int totalSwaps = 0;
    int insertions = 0;

    public HeapVisualizer() {
        setTitle("Heap Insertion Visualizer");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Top panel for controls
        JPanel topPanel = new JPanel();
        inputField = new JTextField(5);
        insertButton = new JButton("Insert");
        resetButton = new JButton("Reset");
        randomButton = new JButton("Random Insert");
        swapLabel = new JLabel("Average swaps per insertion: 0.0");
        topPanel.add(new JLabel("Enter number:"));
        topPanel.add(inputField);
        topPanel.add(insertButton);
        topPanel.add(randomButton);
        topPanel.add(resetButton);
        topPanel.add(swapLabel);
        add(topPanel, BorderLayout.NORTH);

        // Drawing panel
        JPanel drawPanel = new JPanel() {
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawHeap(g);
            }
        };
        add(drawPanel, BorderLayout.CENTER);

        // Button actions
        insertButton.addActionListener(e -> {
            try {
                int value = Integer.parseInt(inputField.getText());
                int swaps = insertHeap(value);
                totalSwaps += swaps;
                insertions++;
                updateSwapLabel();
                drawPanel.repaint();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Enter a valid integer!");
            }
        });

        resetButton.addActionListener(e -> {
            heap.clear();
            totalSwaps = 0;
            insertions = 0;
            updateSwapLabel();
            drawPanel.repaint();
        });

        randomButton.addActionListener(e -> {
            Random rand = new Random();
            int value = rand.nextInt(100);
            int swaps = insertHeap(value);
            totalSwaps += swaps;
            insertions++;
            updateSwapLabel();
            drawPanel.repaint();
        });
    }

    // Heap insertion logic with swap counting
    int insertHeap(int val) {
        heap.add(val);
        int swaps = 0;
        int current = heap.size() - 1;

        while (current > 0) {
            int parent = (current - 1) / 2;
            if (heap.get(current) < heap.get(parent)) { // Min-heap
                int temp = heap.get(current);
                heap.set(current, heap.get(parent));
                heap.set(parent, temp);
                current = parent;
                swaps++;
            } else {
                break;
            }
        }
        return swaps;
    }

    void updateSwapLabel() {
        double average = insertions == 0 ? 0 : (double) totalSwaps / insertions;
        swapLabel.setText(String.format("Average swaps per insertion: %.2f", average));
    }

    // Draw heap nodes as circles with lines
    void drawHeap(Graphics g) {
        if (heap.isEmpty()) return;

        int startX = getWidth() / 2;
        int startY = 50;
        int levelGap = 70;

        for (int i = 0; i < heap.size(); i++) {
            int level = (int) (Math.log(i + 1) / Math.log(2));
            int indexInLevel = i - ((1 << level) - 1);
            int nodesInLevel = 1 << level;
            int xGap = getWidth() / (nodesInLevel + 1);
            int x = xGap * (indexInLevel + 1);
            int y = startY + level * levelGap;

            // Draw line to parent
            if (i != 0) {
                int parent = (i - 1) / 2;
                int parentLevel = (int) (Math.log(parent + 1) / Math.log(2));
                int parentIndex = parent - ((1 << parentLevel) - 1);
                int parentXGap = getWidth() / ((1 << parentLevel) + 1);
                int parentX = parentXGap * (parentIndex + 1);
                int parentY = startY + parentLevel * levelGap;
                g.setColor(Color.BLACK);
                g.drawLine(parentX + 15, parentY + 15, x + 15, y + 15);
            }

            // Draw node
            g.setColor(Color.BLUE);
            g.fillOval(x, y, 30, 30);
            g.setColor(Color.WHITE);
            g.drawString(heap.get(i) + "", x + 10, y + 20);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HeapVisualizer().setVisible(true));
    }
}