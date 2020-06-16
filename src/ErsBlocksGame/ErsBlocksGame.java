package ErsBlocksGame;
/** 
* File: ErsBlocksGame.java
 * User: Administrator
 * Date: Dec 15, 2003
 * Describe: ����˹����� Java ʵ��
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * ��Ϸ���࣬�̳�JFrame�࣬������Ϸ��ȫ�ֿ��ơ�
 * �ں�
 * 1, һ��GameCanvas�������ʵ�����ã�
 * 2, һ�����浱ǰ���(ErsBlock)ʵ�������ã�
 * 3, һ�����浱ǰ������壨ControlPanel��ʵ��������;
 */
public class ErsBlocksGame extends JFrame {
	/**
	 *  ÿ����һ�мƶ��ٷ�
	 */
	public final static int PER_LINE_SCORE = 100;
	/**
	 * �����ٷ��Ժ�������
	 */
	public final static int PER_LEVEL_SCORE = PER_LINE_SCORE * 20;
	/**
	 * �������10��
	 */
	public final static int MAX_LEVEL = 10;
	/**
	 * Ĭ�ϼ�����5
	 */
	public final static int DEFAULT_LEVEL = 5;


   //һ��GameCanvas�������ʵ�����ã�
 
 
	private GameCanvas canvas;
   // һ�����浱ǰ���(ErsBlock)ʵ�������ã�
	private ErsBlock block;
	
    // һ�����浱ǰ������壨ControlPanel��ʵ��������;
	private ControlPanel ctrlPanel;
    
	private boolean playing = false;
	
	private JMenuBar bar = new JMenuBar();
	//�˵�������4���˵�
	private JMenu
	        mGame = new JMenu("��Ϸ"),
			mControl = new JMenu("����"),
			mWindowStyle = new JMenu("���ڷ��"),
			mInfo = new JMenu("����");

	//4���˵��зֱ�����Ĳ˵���
	private JMenuItem
	        miNewGame = new JMenuItem("����Ϸ"),
			miSetBlockColor = new JMenuItem("���÷�����ɫ"),
			miSetBackColor = new JMenuItem("���ñ�����ɫ"),
			miTurnHarder = new JMenuItem("�����Ѷ�"),
			miTurnEasier = new JMenuItem("�����Ѷ�"),
			miExit = new JMenuItem("�˳�"),

			miPlay = new JMenuItem("��ʼ"),
			miPause = new JMenuItem("��ͣ"),
			miResume = new JMenuItem("����"),
			miStop = new JMenuItem("ֹͣ"),

			miAuthor = new JMenuItem("���� : ����"),
			miSourceInfo = new JMenuItem("�汾��1.0");

         //���ô��ڷ��Ĳ˵�

        	private JCheckBoxMenuItem
	        miAsWindows = new JCheckBoxMenuItem("Windows"),
			miAsMotif = new JCheckBoxMenuItem("Motif"),
			miAsMetal = new JCheckBoxMenuItem("Metal", true);

	/**
	 * ����Ϸ��Ĺ��캯��
	 * @param title String�����ڱ���
	 */
	public ErsBlocksGame(String title) {
		super(title);

	//��ʼ���ڵĴ�С���û��ɵ���
		setSize(315, 392);
		Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
    
	 //����Ϸ����������Ļ����
		setLocation((scrSize.width - getSize().width) / 2,
		        (scrSize.height - getSize().height) / 2);
      
	//�����˵�
		createMenu();

		Container container = getContentPane();
	
	// ���ֵ�ˮƽ����֮����6�����صľ���
		container.setLayout(new BorderLayout(6, 0));
    
	 // ����20������ߣ�12����������Ϸ����
		canvas = new GameCanvas(20, 12);
	 
	 //����һ���������
		ctrlPanel = new ControlPanel(this);

	 //��Ϸ�����Ϳ������֮�����Ұڷ�
		container.add(canvas, BorderLayout.CENTER);
		container.add(ctrlPanel, BorderLayout.EAST);
    
	//���Ӵ��ڼ�����
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				stopGame();
				System.exit(0);
			}
		});

    //���ӹ�������������һ�������ı��С���͵���
	//fanning()�������Զ���������ĳߴ�
		addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent ce) {
				canvas.fanning();
			}
		});

		show();    //setVisiable
		
		// ���ݴ��ڵĴ�С���Զ���������ĳߴ�
		canvas.fanning();
		
	}

	/**
	 * ����Ϸ����λ��
	 */
	public void reset() {
		ctrlPanel.reset();     //���ƴ��ڸ�λ
		canvas.reset();        //��Ϸ���帴λ

	}

	/**
	 * �ж���Ϸ�Ƿ��ڽ���
	 * @return boolean, true-�������У�false-�Ѿ�ֹͣ
	 */
	public boolean isPlaying() {
		return playing;
	}

	/**
	 * �õ���ǰ��Ŀ�
	 * @return ErsBlock, ��ǰ��������
	 */
	public ErsBlock getCurBlock() {
		return block;
	}

	/**
	 * �õ���ǰ����
	 * @return GameCanvas, ��ǰ����������
	 */
	public GameCanvas getCanvas() {
		return canvas;
	}

	/**
	 * ��ʼ��Ϸ
	 */
	public void playGame() {
		play();
		ctrlPanel.setPlayButtonEnable(false);
		miPlay.setEnabled(false);
		ctrlPanel.requestFocus();
	}

	/**
	 * ��Ϸ��ͣ
	 */
	public void pauseGame() {
		if (block != null) block.pauseMove();

		ctrlPanel.setPauseButtonLabel(false);
		miPause.setEnabled(false);
		miResume.setEnabled(true);
	}

	/**
	 * ����ͣ�е���Ϸ����
	 */
	public void resumeGame() {
		if (block != null) block.resumeMove();
		ctrlPanel.setPauseButtonLabel(true);
		miPause.setEnabled(true);
		miResume.setEnabled(false);
		ctrlPanel.requestFocus();
	}

	/**
	 * �û�ֹͣ��Ϸ
	 */
	public void stopGame() {
		playing = false;
		if (block != null) block.stopMove();
		miPlay.setEnabled(true);
		miPause.setEnabled(true);
		miResume.setEnabled(false);
		ctrlPanel.setPlayButtonEnable(true);
		ctrlPanel.setPauseButtonLabel(true);
	}

	/**
	 * �õ���ǰ��Ϸ�����õ���Ϸ�Ѷ�
	 * @return int, ��Ϸ�Ѷ�1��MAX_LEVEL��10
	 */
	public int getLevel() {
		return ctrlPanel.getLevel();
	}

	/**
	 * ���û�������Ϸ�Ѷ�ϵ��
	 * @param level int, ��Ϸ�Ѷ�ϵ��Ϊ1��MAX_LEVEL��10
	 */
	public void setLevel(int level) {
		if (level < 11 && level > 0) ctrlPanel.setLevel(level);
	}

	/**
	 * �õ���Ϸ����
	 * @return int, ���֡�
	 */
	public int getScore() {
		if (canvas != null) return canvas.getScore();
		return 0;
	}

	/**
	 * �õ����ϴ�������������Ϸ���֣������Ժ󣬴˻�������
	 * @return int, ���֡�
	 */
	public int getScoreForLevelUpdate() {
		if (canvas != null) return canvas.getScoreForLevelUpdate();
		return 0;
	}

	/**
	 * �������ۼƵ�һ��������ʱ����һ�μ�
	 * @return boolean, ture-���³ɹ�, false-����ʧ��
	 */
	public boolean levelUpdate() {
		int curLevel = getLevel();
		if (curLevel < MAX_LEVEL) {
			setLevel(curLevel + 1);
			canvas.resetScoreForLevelUpdate();
			return true;
		}
		return false;
	}

	/**
	 * ��Ϸ��ʼ
	 */
	private void play() {
		reset();
		playing = true;
		Thread thread = new Thread(new Game());
		thread.start();
	}

	/**
	 * ������Ϸ������
	 */
	private void reportGameOver() {
		JOptionPane.showMessageDialog(this, "��Ϸ����!");
	}

	/**
	 * ���������ô��ڲ˵�
	 */
	private void createMenu() {
		bar.add(mGame);
		bar.add(mControl);
		bar.add(mWindowStyle);
		bar.add(mInfo);

		mGame.add(miNewGame);
		mGame.addSeparator();
		mGame.add(miSetBlockColor);
		mGame.add(miSetBackColor);
		mGame.addSeparator();
		mGame.add(miTurnHarder);
		mGame.add(miTurnEasier);
		mGame.addSeparator();
		mGame.add(miExit);

		mControl.add(miPlay);
		mControl.add(miPause);
		mControl.add(miResume);
		mControl.add(miStop);

		mWindowStyle.add(miAsWindows);
		mWindowStyle.add(miAsMotif);
		mWindowStyle.add(miAsMetal);

		mInfo.add(miAuthor);
		mInfo.add(miSourceInfo);

		setJMenuBar(bar);

		miPause.setAccelerator(
		        KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_MASK));
		miResume.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0));

		miNewGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				stopGame();
				reset();
				setLevel(DEFAULT_LEVEL);
			}
		});
        //JColorChooser���ṩһ����׼��Gui���������û�ѡ��ɫ��
		//ʹ��JColorChooser�ķ���ѡ�񷽿����ɫ
		miSetBlockColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				Color newFrontColor =
				        JColorChooser.showDialog(ErsBlocksGame.this,
				                "���÷�����ɫ", canvas.getBlockColor());
				if (newFrontColor != null)
					canvas.setBlockColor(newFrontColor);
			}
		}); 
	
	    //ʹ��JColorChooser�ķ���ѡ����Ϸ���ı�����ɫ
		miSetBackColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				Color newBackColor =
				        JColorChooser.showDialog(ErsBlocksGame.this,
				                "���ñ�����ɫ", canvas.getBackgroundColor());
				if (newBackColor != null)
					canvas.setBackgroundColor(newBackColor);
			}
		});

		//ʹ��Ϸ���Ѷȼ������ӵĲ˵���
		miTurnHarder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				int curLevel = getLevel();
				if (curLevel < MAX_LEVEL) setLevel(curLevel + 1);
			}
		});
		//ʹ��Ϸ���Ѷȼ��𽵵͵Ĳ˵���
		miTurnEasier.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				int curLevel = getLevel();
				if (curLevel > 1) setLevel(curLevel - 1);
			}
		});
        //�˳���Ϸ�Ĳ˵���
		miExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				System.exit(0);
			}
		});
        //��ʼ��Ϸ�Ĳ˵���
		miPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				playGame();
			}
		});
		
		//��ͣ��Ϸ�Ĳ˵���
		miPause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				pauseGame();
			}
		});
		
		//�ָ���Ϸ�Ĳ˵���
		miResume.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				resumeGame();
			}
		});
		//ֹͣ��Ϸ�Ĳ˵���
		miStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				stopGame();
			}
		});
        
		//����������Ϸ�Ĵ��ڷ��������˵���
		miAsWindows.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				String plaf = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
				setWindowStyle(plaf);
				canvas.fanning();
				ctrlPanel.fanning();
				miAsWindows.setState(true);
				miAsMetal.setState(false);
				miAsMotif.setState(false);
			}
		});
		miAsMotif.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				String plaf = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
				setWindowStyle(plaf);
				canvas.fanning();
				ctrlPanel.fanning();
				miAsWindows.setState(false);
				miAsMetal.setState(false);
				miAsMotif.setState(true);
			}
		});
		miAsMetal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				String plaf = "javax.swing.plaf.metal.MetalLookAndFeel";
				setWindowStyle(plaf);
				canvas.fanning();
				ctrlPanel.fanning();
				miAsWindows.setState(false);
				miAsMetal.setState(true);
				miAsMotif.setState(false);
			}
		});
	}

	/**
	 * �����ִ����ô������
	 * @param plaf String, ������۵�����
	 */
	private void setWindowStyle(String plaf) {
		try {
          //�趨�û���������
			UIManager.setLookAndFeel(plaf);
   
		 //���û�����ĳɵ�ǰ�趨�����
			SwingUtilities.updateComponentTreeUI(this);
		} catch (Exception e) {
		}
	}

	/**
	 * һ����Ϸ���̣�ʵ����Runnable�ӿ�
	 * һ����Ϸ��һ����ѭ���������ѭ���У�ÿ��100���룬
	 * �����Ϸ�еĵ�ǰ���Ƿ��Ѿ������ˣ����û�У�
	 * �ͼ����ȴ�����������ˣ��Ϳ���û��ȫ�������У�
	 * ����о�ɾ��������Ϊ��Ϸ�߼ӷ֣�ͬʱ�������һ��
	 * �µĵ�ǰ�飬����������������ٶȣ��Զ����䡣
	 * ���²���һ����ʱ���ȼ�黭����˵�һ���Ƿ��Ѿ�
	 * ��ռ�ˣ�����ǣ�������ʾ����Ϸ�������ˡ�
	 */
	private class Game implements Runnable {
		public void run() {
			//������ɿ�ĳ�ʼ�е�λ��
			//������ɿ�ĳ�ʼ��̬��28��֮һ��
			int col = (int) (Math.random() * (canvas.getCols() - 3)),
			    style = ErsBlock.STYLES[(int) (Math.random() * 7)][(int) (Math.random() * 4)];

			while (playing) {
				if (block != null) {    //��һ��ѭ��ʱ��blockΪ��
					if (block.isAlive()) {
						try {
							Thread.currentThread().sleep(100);
						} catch (InterruptedException ie) {
							ie.printStackTrace();
						}
						continue;
					}
				}

				checkFullLine();        //����Ƿ���ȫ��������

				if (isGameOver()) {     //�����Ϸ�Ƿ�Ӧ�ý�����
					miPlay.setEnabled(true);
					miPause.setEnabled(true);
					miResume.setEnabled(false);
					ctrlPanel.setPlayButtonEnable(true);
					ctrlPanel.setPauseButtonLabel(true);

					reportGameOver();
					return;
				}
                //����һ����Ϸ��
				block = new ErsBlock(style, -1, col, getLevel(), canvas);

				//��Ϊ�߳̿�ʼ����
				block.start();
	
			//���������һ����ĳ�ʼ�е�λ��
			//���������һ����ĳ�ʼ��̬��28��֮һ��
				col = (int) (Math.random() * (canvas.getCols() - 3));
				style = ErsBlock.STYLES[(int) (Math.random() * 7)][(int) (Math.random() * 4)];

			//�ڿ����������ʾ��һ�������״
				ctrlPanel.setTipStyle(style);
			}
		}

		/**
		 * ��黭�����Ƿ���ȫ�������У�����о�ɾ��֮
		 */
		public void checkFullLine() {
			for (int i = 0; i < canvas.getRows(); i++) {
				int row = -1;
				boolean fullLineColorBox = true;
				for (int j = 0; j < canvas.getCols(); j++) {
                           //����i�У���j���Ƿ�Ϊ��ɫ����
					if (!canvas.getBox(i, j).isColorBox()) {
						   //�ǲ�ɫ���飬
						fullLineColorBox = false;
						break;
						//�˳���ѭ���������һ��
					}
				}
				if (fullLineColorBox) {
					row = i--;
					canvas.removeLine(row);
					//��������������ȥ
				}
			}
		}

		/**
		 * ��������Ƿ�ռ���ж���Ϸ�Ƿ��Ѿ������ˡ�
		 * @return boolean, true-��Ϸ�����ˣ�false-��Ϸδ����
		 */
		private boolean isGameOver() {
			for (int i = 0; i < canvas.getCols(); i++) {
				ErsBox box = canvas.getBox(0, i);
				if (box.isColorBox()) return true;
			}
			return false;
		}
	}

	/**
	 * ������ں���
	 * @param args String[], �����������в���������Ϸ
	 * ����Ҫ�������в���
	 */
	public static void main(String[] args) {
		new ErsBlocksGame("����˹������Ϸ");
	}
}
