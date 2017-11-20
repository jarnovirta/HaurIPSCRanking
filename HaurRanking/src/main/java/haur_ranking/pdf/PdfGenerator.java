package haur_ranking.pdf;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPRow;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import haur_ranking.domain.DivisionRanking;
import haur_ranking.domain.DivisionRankingLine;
import haur_ranking.domain.Ranking;

public class PdfGenerator {

	public static void createPdfRankingFile(Ranking ranking, String path) {
		try {
			Document doc = new Document(PageSize.A4, 50, 50, 90, 90);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PdfWriter pdfWriter = PdfWriter.getInstance(doc, baos);
			doc.open();
			doc.add(getTitleParagraph());

			for (DivisionRanking divisionRanking : ranking.getDivisionRankings()) {
				doc.add(getDivisionRankingParagraph(divisionRanking));
			}

			doc.close();
			writeToFile(baos, path);

		} catch (Exception e) {

		}
	}

	private static Paragraph getDivisionRankingParagraph(DivisionRanking divisionRanking) {
		Paragraph divisionRankingPara = new Paragraph();
		divisionRankingPara.setSpacingBefore(40);
		divisionRankingPara.setAlignment(Element.ALIGN_CENTER);
		divisionRankingPara.add(new Paragraph(new Chunk(divisionRanking.getDivision().toString())));

		PdfPTable table = new PdfPTable(5);
		table.setWidthPercentage(80);
		table.setHorizontalAlignment(Element.ALIGN_CENTER);
		try {

			table.setWidths(new float[] { 40, 200, 40, 50, 80 });

			// Write table header row

			PdfPCell cell = new PdfPCell(new Paragraph(new Chunk("Sija")));
			cell.setPadding(5);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(cell);

			cell = new PdfPCell(new Paragraph(new Chunk("Nimi")));
			cell.setPadding(5);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(cell);

			cell = new PdfPCell(new Paragraph(new Chunk("%")));
			cell.setPadding(5);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(cell);

			cell = new PdfPCell(new Paragraph(new Chunk("HF-ka")));
			cell.setPadding(5);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(cell);

			cell = new PdfPCell(new Paragraph(new Chunk("Tuloksia")));
			cell.setPadding(5);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(cell);

			int position = 1;
			for (DivisionRankingLine line : divisionRanking.getRankingLines()) {
				cell = new PdfPCell(new Paragraph(new Chunk(position + ".")));
				cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
				cell.setPadding(5);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);

				table.addCell(cell);

				cell = new PdfPCell(new Paragraph(
						new Chunk(line.getCompetitor().getFirstName() + " " + line.getCompetitor().getLastName())));
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell.setPadding(5);
				cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
				table.addCell(cell);

				cell = new PdfPCell(new Paragraph(new Chunk(line.getResultPercentage() + "%")));
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setPadding(5);
				cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
				table.addCell(cell);

				double averageHf = round(line.getBestHitFactorsAverage(), 2);
				cell = new PdfPCell(new Paragraph(new Chunk(String.valueOf(averageHf))));
				cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
				cell.setPadding(5);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				table.addCell(cell);

				cell = new PdfPCell(new Paragraph(new Chunk(String.valueOf(line.getResultsCount()))));
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setPadding(5);
				cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
				table.addCell(cell);

				position++;
			}

			boolean oddRow = true;
			for (PdfPRow row : table.getRows()) {
				boolean lastRow = false;
				if (table.getRows().indexOf(row) == table.getRows().size() - 1) {
					lastRow = true;
				}
				for (PdfPCell tableCell : row.getCells()) {
					tableCell.setBackgroundColor(oddRow ? BaseColor.LIGHT_GRAY : BaseColor.WHITE);
					if (lastRow) {
						tableCell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
					}
				}
				oddRow = !oddRow;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		divisionRankingPara.add(table);
		return divisionRankingPara;

	}

	private static Paragraph getTitleParagraph() {
		try {
			Paragraph para = new Paragraph();
			para.setIndentationLeft(15);
			para.setSpacingAfter(20);

			PdfPTable table = new PdfPTable(new float[] { 20, 80 });
			java.awt.Image awtImage = ImageIO.read(ClassLoader.getSystemResource("images/medium_haur_logo.png"));
			Image pdfImage = Image.getInstance(awtImage, null);
			PdfPCell cell = new PdfPCell();
			cell.setBorder(Rectangle.NO_BORDER);
			cell.addElement(pdfImage);
			table.addCell(cell);
			cell = new PdfPCell();
			cell.addElement(new Chunk("HAUR Ranking"));
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

	public static double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		long factor = (long) Math.pow(10, places);
		value = value * factor;
		long tmp = Math.round(value);
		return (double) tmp / factor;
	}

	private static void writeToFile(ByteArrayOutputStream baos, String path) {
		try (OutputStream outputStream = new FileOutputStream(path + "HaurRanking.pdf")) {
			baos.writeTo(outputStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
