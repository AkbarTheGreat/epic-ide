package org.epic.perleditor.editors;

import java.util.HashMap;

import org.eclipse.swt.graphics.RGB;
import org.eclipse.jface.text.DefaultAutoIndentStrategy;
import org.eclipse.jface.text.IAutoIndentStrategy;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.rules.BufferedRuleBasedScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.IAnnotationHover;
import org.eclipse.jface.text.source.ISourceViewer;
import org.epic.perleditor.PerlEditorPlugin;
import org.epic.perleditor.editors.perl.PerlAutoIndentStrategy;
import org.epic.perleditor.editors.perl.PerlCompletionProcessor;
import org.epic.perleditor.editors.perl.PerlDoubleClickSelector;
import org.eclipse.ui.editors.text.TextEditor;

import org.epic.perleditor.editors.util.PerlColorProvider;
import org.epic.perleditor.editors.util.PreferenceUtil;

import org.eclipse.jface.preference.IPreferenceStore;
import org.epic.perleditor.preferences.PreferenceConstants;

import cbg.editor.*;
import cbg.editor.rules.*;

import org.eclipse.jface.util.PropertyChangeEvent;

/**
 * @author luelljoc
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class PerlSourceViewerConfiguration

//extends SourceViewerConfiguration {
		extends ColoringSourceViewerConfiguration {
	IPreferenceStore fStore;

	TextEditor fTextEditor;

	/**
	 * Single token scanner.
	 */

	static class SingleTokenScanner extends BufferedRuleBasedScanner {
		public SingleTokenScanner(TextAttribute attribute) {
			setDefaultReturnToken(new Token(attribute));
		}
	}

	protected void adaptToPreferenceChange(PropertyChangeEvent event) {
		super.adaptToPreferenceChange(event);
	}

	/**
	 *  
	 */
	public PerlSourceViewerConfiguration(IPreferenceStore store,
			TextEditor textEditor) {
		//Changed for version 3.0 of Colorer Plugin
		//super(new ColorManager(store),
		// EditorPlugin.getDefault().getEditorTools());
		super(new ColorManager(store));

		//setMode(Modes.getMode("perl.xml"));
		/*
		 * String filename;
		 *  // If we can't get the filname use default name try { filename =
		 * textEditor.getEditorInput().getName(); } catch(Exception e) {
		 * filename = "input.pl"; }
		 * 
		 * setFilename(filename);
		 */
		fStore = store;
		fTextEditor = textEditor;
	}

	private IPreferenceStore getPreferenceStore() {
		return PerlEditorPlugin.getDefault().getPreferenceStore();
	}

	public IAnnotationHover getAnnotationHover(ISourceViewer sourceViewer) {
		return new PerlAnnotationHover();
	}

	/*
	 * Method declared on SourceViewerConfiguration
	 */
	public IAutoIndentStrategy getAutoIndentStrategy(
			ISourceViewer sourceViewer, String contentType) {
		return (IDocument.DEFAULT_CONTENT_TYPE.equals(contentType) ? new PerlAutoIndentStrategy()
				: new DefaultAutoIndentStrategy());
	}

	/*
	 * Method declared on SourceViewerConfiguration
	 */
	/*
	 * public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
	 * return new String[] { IDocument.DEFAULT_CONTENT_TYPE,
	 * PerlPartitionScanner.PERL_MULTI_LINE_COMMENT }; }
	 */

	/*
	 * Method declared on SourceViewerConfiguration
	 */
	public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {

		ContentAssistant assistant = new ContentAssistant();

		//assistant.setContentAssistProcessor(new
		// PerlCompletionProcessor(fTextEditor),
		// IDocument.DEFAULT_CONTENT_TYPE);
		// Enable content assist for all content types
		String[] contentTypes = this.getConfiguredContentTypes(sourceViewer);
		for (int i = 0; i < contentTypes.length; i++) {
			assistant.setContentAssistProcessor(new PerlCompletionProcessor(
					fTextEditor), contentTypes[i]);
		}

		assistant.enableAutoActivation(true);
		assistant.enableAutoInsert(true);
		assistant.setAutoActivationDelay(500);
		assistant
				.setProposalPopupOrientation(ContentAssistant.PROPOSAL_OVERLAY);
		assistant
				.setContextInformationPopupOrientation(ContentAssistant.CONTEXT_INFO_ABOVE);
		assistant.setContextInformationPopupBackground(PerlColorProvider
				.getColor(new RGB(150, 150, 0)));
		assistant.setProposalSelectorBackground(PerlColorProvider
				.getColor(new RGB(254, 241, 233)));
		assistant
				.setInformationControlCreator(getInformationControlCreator(sourceViewer));

		return assistant;
	}

	/*
	 * Method declared on SourceViewerConfiguration
	 */
	/*
	 * public String getDefaultPrefix(ISourceViewer sourceViewer, String
	 * contentType) { return (IDocument.DEFAULT_CONTENT_TYPE.equals(contentType) ?
	 * "//" : null); //$NON-NLS-1$
	 *  }
	 */
	public String[] getDefaultPrefixes(ISourceViewer sourceViewer,
			String contentType) {
		return (new String[] { "#", "" }); //$NON-NLS-1$

	}

	/*
	 * Method declared on SourceViewerConfiguration
	 */
	public ITextDoubleClickStrategy getDoubleClickStrategy(
			ISourceViewer sourceViewer, String contentType) {
		return new PerlDoubleClickSelector();
	}

	/*
	 * Method declared on SourceViewerConfiguration
	 */
	public String[] getIndentPrefixes(ISourceViewer sourceViewer,
			String contentType) {
		//return new String[] { "\t", " " }; //$NON-NLS-1$ //$NON-NLS-2$
		return new String[] { PreferenceUtil.getTab(0), "\t" };
	}

	/*
	 * Method declared on SourceViewerConfiguration
	 */
	public int getTabWidth(ISourceViewer sourceViewer) {
		//return 4;
		return fStore.getInt(PreferenceConstants.EDITOR_TAB_WIDTH);
	}

	/*
	 * Method declared on SourceViewerConfiguration
	 */
	public ITextHover getTextHover(ISourceViewer sourceViewer,
			String contentType) {
		return new PerlTextHover();
	}
}