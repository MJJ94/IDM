import org.junit.Test
import org.eclipse.emf.common.util.URI
import static org.junit.Assert.*
import fr.istic.videoGen.OptionalMedia
import fr.istic.videoGen.Media
import fr.istic.videoGen.MandatoryMedia
import fr.istic.videoGen.VideoDescription
import java.io.File
import fr.istic.videoGen.AlternativesMedia
import fr.istic.videoGen.MediaDescription
import java.util.ArrayList
import java.util.HashMap
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.FileReader
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths

class VideoGenTest1XtendVersion {
	var private listMan = new ArrayList<String>();
	var private listOp = new ArrayList<String>();
	var private listAlt = new ArrayList<String>();
	var private mapSizes = new HashMap<String, Long>
	var private variants = new ArrayList<ArrayList<String>>

	def void initTest(String specification) {
		this.listMan = new ArrayList<String>();
		this.listOp = new ArrayList<String>();
		this.listAlt = new ArrayList<String>();
		this.mapSizes = new HashMap<String, Long>
		cleanDirectories()
		val videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI(specification))
		for (Media m : videoGen.medias) {
			if (m instanceof MandatoryMedia) {
				val man = m as MandatoryMedia
				if (man.description instanceof VideoDescription) {
					val des = man.description as VideoDescription
					val f = new File(des.location)
					this.listMan.add(des.location);
					this.mapSizes.put(des.location, f.length)
				}
			} else if (m instanceof OptionalMedia) {
				val op = m as OptionalMedia
				if (op.description instanceof VideoDescription) {
					val des = op.description as VideoDescription
					val f = new File(des.location)
					this.listOp.add(des.location);
					this.mapSizes.put(des.location, f.length)
				}
			} else if (m instanceof AlternativesMedia) {
				val alt = m as AlternativesMedia
				for (MediaDescription malt : alt.medias) {
					if (malt instanceof VideoDescription) {
						val des = malt as VideoDescription
						val f = new File(des.location)
						this.listAlt.add(des.location)
						this.mapSizes.put(des.location, f.length)
					}
				}
			}
		}
		this.variants = Utils.calculateVariants(listMan, listOp, listAlt)
	}

	def int nbVariants(int sizeMan, int sizeOpt, int sizeAlt) {
		var result = 1
		// pour le cas de plusieurs optionels
		var nbOp = Math.pow(2, sizeOpt)

		if (sizeOpt > 0) {
			result *= nbOp.intValue
		}

		if (sizeAlt > 0) {
			result *= sizeAlt
		}

		return result
	}

	def int nbLinesCSV(String path) {
		val File file = new File(path)
		var result = 0
		if (file.exists) {
			try {
				val stdInput = new BufferedReader(new FileReader(file))
				var c = 0
				while (c > -1) {
					var line = stdInput.readLine
					if (!line.contains("id")) {
						result++
					}
					c = stdInput.read
				}
				return result
			} catch (IOException e) {
				System.err.println(e)
				return -1
			}
		}
		System.err.println("the File: " + path + " doesn't exist")
		return -2
	}

	def void cleanDirectories() {
		val outputs = "./outputs/"
		var results = new File("./results/")
		var videoOutputs = new File(outputs + "videos/")
		var gifsOutputs = new File(outputs + "gifs/")
		var iconsOutputs = new File(outputs + "icons/")
		var ArrayList<File> files = new ArrayList
		files.addAll(results.listFiles)
		files.addAll(videoOutputs.listFiles)
		files.addAll(gifsOutputs.listFiles)
		files.addAll(iconsOutputs.listFiles)
		for (File file : files) {
			Files.delete(Paths.get(file.path))
		}
	}

	@Test
	def void nbVariants() {
		initTest("only_alternatives.videogen")
		val nbVariants = nbVariants(listMan.size(), listOp.size(), listAlt.size)
		assertEquals(nbVariants, variants.size(), 0)
	}

	@Test
	def void nbLinesCSV() {
		initTest("only_alternatives.videogen")
		val nbVariants = nbVariants(listMan.size(), listOp.size(), listAlt.size)
		val String csvPath = Utils.generateCSV(variants, mapSizes, listMan, listOp, listAlt)
		val nbLinesCsv = nbLinesCSV(csvPath)
		assertEquals(nbLinesCsv, nbVariants, 0)
	}

	def int getNbIcons() {
		var int result = 0
		val iconFolderPath = "./outputs/icons/"
		var iconFolder = new File(iconFolderPath)
		var ArrayList<File> icons = new ArrayList(iconFolder.listFiles)

		for (File icon : icons) {
			result++
		}

		return result
	}

	@Test
	def void nbIcons() {
		initTest("specification.videogen")
		var ArrayList<String> allVideos = new ArrayList<String>()

		allVideos.addAll(listMan)
		allVideos.addAll(listOp)
		allVideos.addAll(listAlt)

		Utils.generateIcons(allVideos)
		val nbIcons = getNbIcons()

		assertEquals(allVideos.size(), nbIcons, 0)
	}
}
