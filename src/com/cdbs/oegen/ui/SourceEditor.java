/**
 * 
 */
package com.cdbs.oegen.ui;

import javax.swing.JComponent;
import javax.swing.JSplitPane;

import com.cdbs.oegen.data.Source;

/**
 * @author toxn
 *
 */
public class SourceEditor
extends JComponent
{

	/**
	 * @author toxn
	 * 
	 */
	public class TranscriptionEditor
	extends JComponent
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Source source;

	private final JSplitPane rootSplitPane;

	private final TranscriptionEditor transcriptEditor;

	SourceEditor(Source src)
	{
		rootSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		getRootPane().add(rootSplitPane);

		transcriptEditor = new TranscriptionEditor();
		rootSplitPane.setLeftComponent(transcriptEditor);
	}

	/**
	 * @return the source
	 */
	public Source getSource()
	{
		return source;
	}

	/**
	 * @param source
	 *            the source to set
	 */
	public void setSource(Source source)
	{
		// FIXME update display to new source
		this.source = source;
	}
}
