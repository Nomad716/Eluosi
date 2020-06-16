package ErsBlocksGame;
/** 
* File: ErsBlocksGame.java
 * User: Administrator
 * Date: Dec 15, 2003
 * Describe: 俄罗斯方块的 Java 实现
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * 游戏主类，继承JFrame类，负责游戏的全局控制。
 * 内含
 * 1, 一个GameCanvas画布类的实例引用，
 * 2, 一个保存当前活动块(ErsBlock)实例的引用，
 * 3, 一个保存当前控制面板（ControlPanel）实例的引用;
 */
public class ErsBlocksGame extends JFrame {
	/**
	 *  每填满一行计多少分
	 */
	public final static int PER_LINE_SCORE = 100;
	/**
	 * 积多少分以后能升级
	 */
	public final static int PER_LEVEL_SCORE = PER_LINE_SCORE * 20;
	/**
	 * 最大级数是10级
	 */
	public final static int MAX_LEVEL = 10;
	/**
	 * 默认级数是5
	 */
	public final static int DEFAULT_LEVEL = 5;


   //一个GameCanvas画布类的实例引用，
 
 
	private GameCanvas canvas;
   // 一个保存当前活动块(ErsBlock)实例的引用，
	private ErsBlock block;
	
    // 一个保存当前控制面板（ControlPanel）实例的引用;
	private ControlPanel ctrlPanel;
    
	private boolean playing = false;
	
	private JMenuBar bar = new JMenuBar();
	//菜单条包含4个菜单
	private JMenu
	        mGame = new JMenu("游戏"),
			mControl = new JMenu("控制"),
			mWindowStyle = new JMenu("窗口风格"),
			mInfo = new JMenu("帮助");

	//4个菜单中分别包含的菜单项
	private JMenuItem
	        miNewGame = new JMenuItem("新游戏"),
			miSetBlockColor = new JMenuItem("设置方块颜色"),
			miSetBackColor = new JMenuItem("设置背景颜色"),
			miTurnHarder = new JMenuItem("增加难度"),
			miTurnEasier = new JMenuItem("降低难度"),
			miExit = new JMenuItem("退出"),

			miPlay = new JMenuItem("开始"),
			miPause = new JMenuItem("暂停"),
			miResume = new JMenuItem("继续"),
			miStop = new JMenuItem("停止"),

			miAuthor = new JMenuItem("作者 : 邱炫"),
			miSourceInfo = new JMenuItem("版本：1.0");

         //设置窗口风格的菜单

        	private JCheckBoxMenuItem
	        miAsWindows = new JCheckBoxMenuItem("Windows"),
			miAsMotif = new JCheckBoxMenuItem("Motif"),
			miAsMetal = new JCheckBoxMenuItem("Metal", true);

	/**
	 * 主游戏类的构造函数
	 * @param title String，窗口标题
	 */
	public ErsBlocksGame(String title) {
		super(title);

	//初始窗口的大小，用户可调控
		setSize(315, 392);
		Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
    
	 //将游戏窗口置于屏幕中央
		setLocation((scrSize.width - getSize().width) / 2,
		        (scrSize.height - getSize().height) / 2);
      
	//创建菜单
		createMenu();

		Container container = getContentPane();
	
	// 布局的水平构件之间有6个象素的距离
		container.setLayout(new BorderLayout(6, 0));
    
	 // 建立20个方块高，12个方块宽的游戏画布
		canvas = new GameCanvas(20, 12);
	 
	 //建立一个控制面板
		ctrlPanel = new ControlPanel(this);

	 //游戏画布和控制面板之间左右摆放
		container.add(canvas, BorderLayout.CENTER);
		container.add(ctrlPanel, BorderLayout.EAST);
    
	//增加窗口监听器
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				stopGame();
				System.exit(0);
			}
		});

    //增加构件的适配器，一旦构件改变大小，就调用
	//fanning()方法，自动调整方格的尺寸
		addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent ce) {
				canvas.fanning();
			}
		});

		show();    //setVisiable
		
		// 根据窗口的大小，自动调整方格的尺寸
		canvas.fanning();
		
	}

	/**
	 * 让游戏“复位”
	 */
	public void reset() {
		ctrlPanel.reset();     //控制窗口复位
		canvas.reset();        //游戏画板复位

	}

	/**
	 * 判断游戏是否还在进行
	 * @return boolean, true-还在运行，false-已经停止
	 */
	public boolean isPlaying() {
		return playing;
	}

	/**
	 * 得到当前活动的块
	 * @return ErsBlock, 当前活动块的引用
	 */
	public ErsBlock getCurBlock() {
		return block;
	}

	/**
	 * 得到当前画布
	 * @return GameCanvas, 当前画布的引用
	 */
	public GameCanvas getCanvas() {
		return canvas;
	}

	/**
	 * 开始游戏
	 */
	public void playGame() {
		play();
		ctrlPanel.setPlayButtonEnable(false);
		miPlay.setEnabled(false);
		ctrlPanel.requestFocus();
	}

	/**
	 * 游戏暂停
	 */
	public void pauseGame() {
		if (block != null) block.pauseMove();

		ctrlPanel.setPauseButtonLabel(false);
		miPause.setEnabled(false);
		miResume.setEnabled(true);
	}

	/**
	 * 让暂停中的游戏继续
	 */
	public void resumeGame() {
		if (block != null) block.resumeMove();
		ctrlPanel.setPauseButtonLabel(true);
		miPause.setEnabled(true);
		miResume.setEnabled(false);
		ctrlPanel.requestFocus();
	}

	/**
	 * 用户停止游戏
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
	 * 得到当前游戏者设置的游戏难度
	 * @return int, 游戏难度1－MAX_LEVEL＝10
	 */
	public int getLevel() {
		return ctrlPanel.getLevel();
	}

	/**
	 * 让用户设置游戏难度系数
	 * @param level int, 游戏难度系数为1－MAX_LEVEL＝10
	 */
	public void setLevel(int level) {
		if (level < 11 && level > 0) ctrlPanel.setLevel(level);
	}

	/**
	 * 得到游戏积分
	 * @return int, 积分。
	 */
	public int getScore() {
		if (canvas != null) return canvas.getScore();
		return 0;
	}

	/**
	 * 得到自上次升级以来的游戏积分，升级以后，此积分清零
	 * @return int, 积分。
	 */
	public int getScoreForLevelUpdate() {
		if (canvas != null) return canvas.getScoreForLevelUpdate();
		return 0;
	}

	/**
	 * 当分数累计到一定的数量时，升一次级
	 * @return boolean, ture-更新成功, false-更新失败
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
	 * 游戏开始
	 */
	private void play() {
		reset();
		playing = true;
		Thread thread = new Thread(new Game());
		thread.start();
	}

	/**
	 * 报告游戏结束了
	 */
	private void reportGameOver() {
		JOptionPane.showMessageDialog(this, "游戏结束!");
	}

	/**
	 * 建立并设置窗口菜单
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
        //JColorChooser类提供一个标准的Gui构件，让用户选择色彩
		//使用JColorChooser的方法选择方块的颜色
		miSetBlockColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				Color newFrontColor =
				        JColorChooser.showDialog(ErsBlocksGame.this,
				                "设置方块颜色", canvas.getBlockColor());
				if (newFrontColor != null)
					canvas.setBlockColor(newFrontColor);
			}
		}); 
	
	    //使用JColorChooser的方法选择游戏面板的背景颜色
		miSetBackColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				Color newBackColor =
				        JColorChooser.showDialog(ErsBlocksGame.this,
				                "设置背景颜色", canvas.getBackgroundColor());
				if (newBackColor != null)
					canvas.setBackgroundColor(newBackColor);
			}
		});

		//使游戏的难度级别增加的菜单项
		miTurnHarder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				int curLevel = getLevel();
				if (curLevel < MAX_LEVEL) setLevel(curLevel + 1);
			}
		});
		//使游戏的难度级别降低的菜单项
		miTurnEasier.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				int curLevel = getLevel();
				if (curLevel > 1) setLevel(curLevel - 1);
			}
		});
        //退出游戏的菜单项
		miExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				System.exit(0);
			}
		});
        //开始游戏的菜单项
		miPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				playGame();
			}
		});
		
		//暂停游戏的菜单项
		miPause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				pauseGame();
			}
		});
		
		//恢复游戏的菜单项
		miResume.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				resumeGame();
			}
		});
		//停止游戏的菜单项
		miStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				stopGame();
			}
		});
        
		//设置整个游戏的窗口风格的三个菜单项
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
	 * 根据字串设置窗口外观
	 * @param plaf String, 窗口外观的描述
	 */
	private void setWindowStyle(String plaf) {
		try {
          //设定用户界面的外观
			UIManager.setLookAndFeel(plaf);
   
		 //将用户界面改成当前设定的外观
			SwingUtilities.updateComponentTreeUI(this);
		} catch (Exception e) {
		}
	}

	/**
	 * 一轮游戏过程，实现了Runnable接口
	 * 一轮游戏是一个大循环，在这个循环中，每隔100毫秒，
	 * 检查游戏中的当前块是否已经到底了，如果没有，
	 * 就继续等待。如果到底了，就看有没有全填满的行，
	 * 如果有就删除它，并为游戏者加分，同时随机产生一个
	 * 新的当前块，让它按所定级别的速度，自动下落。
	 * 当新产生一个块时，先检查画布最顶端的一行是否已经
	 * 被占了，如果是，可以显示“游戏结束”了。
	 */
	private class Game implements Runnable {
		public void run() {
			//随机生成块的初始列的位置
			//随机生成块的初始形态（28种之一）
			int col = (int) (Math.random() * (canvas.getCols() - 3)),
			    style = ErsBlock.STYLES[(int) (Math.random() * 7)][(int) (Math.random() * 4)];

			while (playing) {
				if (block != null) {    //第一次循环时，block为空
					if (block.isAlive()) {
						try {
							Thread.currentThread().sleep(100);
						} catch (InterruptedException ie) {
							ie.printStackTrace();
						}
						continue;
					}
				}

				checkFullLine();        //检查是否有全填满的行

				if (isGameOver()) {     //检查游戏是否应该结束了
					miPlay.setEnabled(true);
					miPause.setEnabled(true);
					miResume.setEnabled(false);
					ctrlPanel.setPlayButtonEnable(true);
					ctrlPanel.setPauseButtonLabel(true);

					reportGameOver();
					return;
				}
                //创建一个游戏块
				block = new ErsBlock(style, -1, col, getLevel(), canvas);

				//作为线程开始运行
				block.start();
	
			//随机生成下一个块的初始列的位置
			//随机生成下一个块的初始形态（28种之一）
				col = (int) (Math.random() * (canvas.getCols() - 3));
				style = ErsBlock.STYLES[(int) (Math.random() * 7)][(int) (Math.random() * 4)];

			//在控制面板中提示下一个块的形状
				ctrlPanel.setTipStyle(style);
			}
		}

		/**
		 * 检查画布中是否有全填满的行，如果有就删除之
		 */
		public void checkFullLine() {
			for (int i = 0; i < canvas.getRows(); i++) {
				int row = -1;
				boolean fullLineColorBox = true;
				for (int j = 0; j < canvas.getCols(); j++) {
                           //检查第i行，第j列是否为彩色方块
					if (!canvas.getBox(i, j).isColorBox()) {
						   //非彩色方块，
						fullLineColorBox = false;
						break;
						//退出内循环，检查下一行
					}
				}
				if (fullLineColorBox) {
					row = i--;
					canvas.removeLine(row);
					//该行已填满，移去
				}
			}
		}

		/**
		 * 根据最顶行是否被占，判断游戏是否已经结束了。
		 * @return boolean, true-游戏结束了，false-游戏未结束
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
	 * 程序入口函数
	 * @param args String[], 附带的命令行参数，该游戏
	 * 不需要带命令行参数
	 */
	public static void main(String[] args) {
		new ErsBlocksGame("俄罗斯方块游戏");
	}
}
