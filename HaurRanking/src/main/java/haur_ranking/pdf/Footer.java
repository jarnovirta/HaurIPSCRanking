package haur_ranking.pdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

public class Footer extends PdfPageEventHelper {
	private boolean showFooterOnPage = false;

	@Override
	public void onEndPage(PdfWriter writer, Document document) {
		if (showFooterOnPage) {
			PdfContentByte cb = writer.getDirectContent();
			Phrase footer = new Phrase("* V‰hint‰‰n 4 tulosta vaaditaan sijoitukseen ja 8 viimeisint‰ huomioidaan.");
			ColumnText.showTextAligned(cb, Element.ALIGN_LEFT, footer, 100, document.bottom() - 40, 0);
			showFooterOnPage = false;
		}
	}

	public void setShowFooterOnPage(boolean showFooterOnPage) {
		this.showFooterOnPage = showFooterOnPage;
	}
}
