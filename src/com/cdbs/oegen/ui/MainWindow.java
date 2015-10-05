package com.cdbs.oegen.ui;

import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JButton;
import javax.swing.JFrame;

import com.cdbs.oegen.data.Universe;

/**
 * 
 * @author toxn
 * 
 */
public final class MainWindow
extends JFrame
{
	private static final String UNIVERSE_PATH = ".oegen/universe"; //$NON-NLS-1$

	JButton personButton;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static void main(String[] args)
	{
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(UNIVERSE_PATH)))
		{
			Universe u = (Universe)ois.readObject();
			Universe.universe = u;
		}
		catch (ClassNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (InvalidClassException e)
		{
			// TODO warn user
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		MainWindow mainWin = new MainWindow(Messages.getString("MainWindow.title")); //$NON-NLS-1$

		mainWin.addWindowListener(new WindowListener()
		{

			@Override
			public void windowActivated(WindowEvent arg0)
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void windowClosed(WindowEvent arg0)
			{
				// TODO Auto-generated method stub
			}

			@Override
			public void windowClosing(WindowEvent arg0)
			{
				try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(UNIVERSE_PATH)))
				{
					oos.writeObject(Universe.universe);
				}
				catch (FileNotFoundException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			@Override
			public void windowDeactivated(WindowEvent arg0)
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void windowDeiconified(WindowEvent arg0)
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void windowIconified(WindowEvent arg0)
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void windowOpened(WindowEvent arg0)
			{
				// TODO Auto-generated method stub

			}
		});
		mainWin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		mainWin.pack();
		mainWin.setVisible(true);
	}

	public MainWindow()
		throws HeadlessException
		{
		setupUI();
		}

	public MainWindow(GraphicsConfiguration gc)
	{
		super(gc);
		setupUI();
	}

	public MainWindow(String title)
		throws HeadlessException
		{
		super(title);
		setupUI();
		}

	public MainWindow(String title, GraphicsConfiguration gc)
	{
		super(title, gc);
		setupUI();
	}

	public void setupUI()
	{
		personButton = new JButton(Messages.getString("MainWindow.personButton_Text")); //$NON-NLS-1$
		personButton.setToolTipText(Messages.getString("MainWindow.personButton_TooltipText")); //$NON-NLS-1$
		add(personButton);
	}

}
