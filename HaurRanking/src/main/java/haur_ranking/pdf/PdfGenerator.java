package haur_ranking.pdf;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPRow;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import haur_ranking.domain.DivisionRanking;
import haur_ranking.domain.DivisionRankingRow;
import haur_ranking.domain.Ranking;
import haur_ranking.utils.DataFormatUtils;
import haur_ranking.utils.DateFormatUtils;

public class PdfGenerator {

	private static Font h1Font = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.BOLD);
	private static Font h2Font = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD);
	private static Font tableHeaderFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
	private static Font defaultFont = new Font(Font.FontFamily.TIMES_ROMAN, 12);
	private static Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
	private static Ranking ranking;
	private static Document doc;
	private static boolean firstPage = true;

	public static void createPdfRankingFile(Ranking rankingData, Ranking compareToRanking, String path) {
		ranking = rankingData;
		try {
			doc = new Document(PageSize.A4, 50, 50, 90, 120);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PdfWriter pdfWriter = PdfWriter.getInstance(doc, baos);
			Footer footer = new Footer(DateFormatUtils.calendarToDateString(ranking.getDate()),
					ranking.getTotalResultsCount(), ranking.getCompetitorsWithRank(),
					ranking.getValidClassifiersCount());
			pdfWriter.setPageEvent(footer);
			doc.open();
			doc.add(getTitleParagraph());
			for (DivisionRanking divisionRanking : ranking.getDivisionRankings()) {
				footer.setShowFooterOnPage(true);
				doc.add(getDivisionRankingParagraph(divisionRanking));
			}
			doc.close();
			writeToFile(baos, path);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static Paragraph getDivisionRankingParagraph(DivisionRanking divisionRanking) {

		Paragraph divisionRankingPara = new Paragraph();
		divisionRankingPara.setSpacingBefore(40);
		divisionRankingPara.setAlignment(Element.ALIGN_CENTER);
		divisionRankingPara
				.add(new Paragraph(new Chunk(divisionRanking.getDivision().toString().toUpperCase() + " ", h2Font)));

		PdfPTable table = new PdfPTable(5);
		table.setWidthPercentage(80);
		table.setHorizontalAlignment(Element.ALIGN_CENTER);
		try {

			table.setWidths(new float[] { 40, 170, 70, 70, 80 });

			// Write table header row

			PdfPCell cell = new PdfPCell(new Paragraph(new Chunk("Sija", tableHeaderFont)));
			cell.setPadding(5);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(cell);

			cell = new PdfPCell(new Paragraph(new Chunk("Nimi", tableHeaderFont)));
			cell.setPadding(5);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(cell);

			cell = new PdfPCell(new Paragraph(new Chunk("%", tableHeaderFont)));
			cell.setPadding(5);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(cell);

			cell = new PdfPCell(new Paragraph(new Chunk("HF-ka", tableHeaderFont)));
			cell.setPadding(5);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(cell);

			String resultsColumnHeader = "Tuloksia*";

			if (firstPage == true) {
				resultsColumnHeader += "*";
				firstPage = false;
			}
			cell = new PdfPCell(new Paragraph(new Chunk(resultsColumnHeader, tableHeaderFont)));
			cell.setPadding(5);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(cell);

			int position = 1;
			for (DivisionRankingRow row : divisionRanking.getDivisionRankingRows()) {
				Font rowFont;
				if (row.isImprovedResult()) {
					rowFont = boldFont;

				} else
					rowFont = defaultFont;
				String positionString = "--";
				if (row.isRankedCompetitor())
					positionString = position + ".";
				cell = new PdfPCell(new Paragraph(new Chunk(positionString, rowFont)));
				cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
				cell.setPadding(5);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);

				table.addCell(cell);

				cell = new PdfPCell(new Paragraph(new Chunk(
						row.getCompetitor().getFirstName() + " " + row.getCompetitor().getLastName(), rowFont)));
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell.setPadding(5);
				cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
				table.addCell(cell);

				String percentageString = "--";
				if (row.isRankedCompetitor())
					percentageString = DataFormatUtils
							.formatTwoDecimalNumberToString(DataFormatUtils.round(row.getResultPercentage(), 2)) + " %";
				cell = new PdfPCell(new Paragraph(new Chunk(percentageString, rowFont)));
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setPadding(5);
				cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
				table.addCell(cell);

				String averageHfString = "--";
				if (row.isRankedCompetitor())
					averageHfString = DataFormatUtils
							.formatTwoDecimalNumberToString(DataFormatUtils.round(row.getHitFactorAverage(), 2));
				cell = new PdfPCell(new Paragraph(new Chunk(averageHfString, rowFont)));
				cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
				cell.setPadding(5);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				table.addCell(cell);

				cell = new PdfPCell(new Paragraph(new Chunk(String.valueOf(row.getResultsCount()), rowFont)));
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setPadding(5);
				cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
				table.addCell(cell);

				position++;
			}

			boolean oddRow = true;
			for (PdfPRow row : table.getRows()) {
				boolean lastRow = table.getRows().indexOf(row) == table.getRows().size() - 1;
				for (PdfPCell tableCell : row.getCells()) {
					tableCell.setBackgroundColor(oddRow ? BaseColor.LIGHT_GRAY : BaseColor.WHITE);
					if (lastRow) {
						tableCell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
					}
				}
				oddRow = !oddRow;
			}

		} catch (

		Exception e) {
			e.printStackTrace();
		}
		divisionRankingPara.add(table);
		return divisionRankingPara;

	}

	private static Paragraph getTitleParagraph() {
		try {
			Paragraph para = new Paragraph();
			para.setSpacingAfter(20);

			PdfPTable table = new PdfPTable(new float[] { 20, 80 });
			java.awt.Image awtImage = ImageIO.read(ClassLoader.getSystemResource("images/medium_haur_logo.png"));
			Image pdfImage = Image.getInstance(awtImage, null);
			PdfPCell cell = new PdfPCell();
			cell.setBorder(Rectangle.NO_BORDER);
			cell.addElement(pdfImage);
			table.addCell(cell);
			cell = new PdfPCell();
			cell.addElement(new Chunk("HAUR Ranking - " + getRankingPdfDateString() + "*", h1Font));
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setPaddingLeft(15);
			cell.setBorder(Rectangle.NO_BORDER);
			table.addCell(cell);
			para.add(table);
			return para;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static void writeToFile(ByteArrayOutputStream baos, String path) {
		try {
			OutputStream outputStream = new FileOutputStream(path);
			baos.writeTo(outputStream);
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static String getRankingPdfDateString() {
		SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");
		return DATE_FORMAT.format(new Date());
	}
}
