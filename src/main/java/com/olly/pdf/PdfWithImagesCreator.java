package com.olly.pdf;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * 
 * @author alessio olivieri
 *
 */
public class PdfWithImagesCreator {
	private final static Logger LOGGER = LogManager.getLogger(PdfWithImagesCreator.class);
	
	public static void main(String[] args) throws Exception {
		new PdfWithImagesCreator().go();
	}
	/**
	 * Assuming there's a folder like:<br>
	 * - /temp/base/magazine1/0.png<br>
	 * - /temp/base/magazine1/1.png<br>
	 * - /temp/base/magazine1/2.png<br>
	 * - /temp/base/magazine1/3.png<br>
	 * - /temp/base/magazine1/4.png<br>
	 * - /temp/base/magazine1/5.png<br>
	 * - /temp/base/magazine2/0.png<br>
	 * - /temp/base/magazine2/1.png<br>
	 * - /temp/base/magazine2/2.png<br>
	 * - /temp/base/magazine2/3.png<br>
	 * - /temp/base/magazine3/0.png<br>
	 * - /temp/base/magazine3/1.png<br>
	 * It creates:<br>
	 * - /temp/base/magazine1.pdf with pages: 0.png, 1.png, 2.png, 3.png, 4.png, 5.png<br>
	 * - /temp/base/magazine2.pdf with pages: 0.png, 1.png, 2.png, 3.png<br>
	 * - /temp/base/magazine3.pdf with pages: 0.png, 1.png<br>
	 * @throws Exception
	 */
	public void go() throws Exception {
		LOGGER.info("doign PDFs");
		File root = new File("/temp/base");
		for (File dir:root.listFiles()) {
			if (dir.isDirectory() && !dir.isHidden()) {
				LOGGER.debug("processing: "+dir);
				Document document = new Document();
				PdfWriter.getInstance(document, new FileOutputStream(new File(root,dir.getName()+".pdf")));
				document.open();
				List<File> files = Arrays.asList(dir.listFiles());
				files.sort((File a, File b)->{
					try {
						Integer na = new Integer(a.getName().substring(0,a.getName().indexOf(".")));
						Integer nb = new Integer(b.getName().substring(0,b.getName().indexOf(".")));
						return na.compareTo(nb);
					} catch (Exception nfe) {
						return a.getName().compareTo(b.getName());
					}
				});
				for (File f :files) {
					if (f.isHidden() || f.isDirectory())
						continue;
					LOGGER.debug("- "+f.getAbsolutePath());
					document.newPage();
					Image image = Image.getInstance(f.getAbsolutePath());
					image.setAbsolutePosition(0, 0);
					image.setBorderWidth(0);
					image.scaleAbsoluteHeight(PageSize.A4.getHeight());
					image.scaleAbsoluteWidth(PageSize.A4.getWidth());
					document.add(image);
				}
				document.close();
			}
		}
		LOGGER.info("PDFs completed");
	}

}

