import org.eclipse.emf.common.util.URI
import fr.istic.videoGen.OptionalMedia
import fr.istic.videoGen.Media
import fr.istic.videoGen.MandatoryMedia
import fr.istic.videoGen.VideoDescription
import java.io.File
import fr.istic.videoGen.AlternativesMedia
import fr.istic.videoGen.MediaDescription
import java.util.ArrayList
import java.util.HashMap

class BigTest {
	var static private listMan = new ArrayList<String>();
	var static private listOp = new ArrayList<String>();
	var static private listAlt = new ArrayList<String>();
	var static private mapSizes = new HashMap<String, Long>
	var static private variants = new ArrayList<ArrayList<String>>

	def static void initMain(String specification, String parentDir) {
		listMan = new ArrayList<String>();
		listOp = new ArrayList<String>();
		listAlt = new ArrayList<String>();
		mapSizes = new HashMap<String, Long>

		val videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI(parentDir + "/" + specification))
		for (Media m : videoGen.medias) {
			if (m instanceof MandatoryMedia) {
				val man = m as MandatoryMedia
				if (man.description instanceof VideoDescription) {
					val des = man.description as VideoDescription
					val f = new File(parentDir + "/" + des.location)
					listMan.add(des.location);
					mapSizes.put(des.location, f.length)
				}
			} else if (m instanceof OptionalMedia) {
				val op = m as OptionalMedia
				if (op.description instanceof VideoDescription) {
					val des = op.description as VideoDescription
					val f = new File(parentDir + "/" + des.location)
					listOp.add(des.location);
					mapSizes.put(des.location, f.length)
				}
			} else if (m instanceof AlternativesMedia) {
				val alt = m as AlternativesMedia
				for (MediaDescription malt : alt.medias) {
					if (malt instanceof VideoDescription) {
						val des = malt as VideoDescription
						val f = new File(parentDir + "/" + des.location)
						listAlt.add(des.location)
						mapSizes.put(des.location, f.length)
					}
				}
			}
		}
	}

	def static ArrayList<File> runVideogens(ArrayList<File> directories) {
		var videoGens = new ArrayList<File>
		for (File dir : directories) {
			var files = new ArrayList(dir.listFiles())
			for (File f : files) {
				var fileName = f.getName();
				if (fileName.contains(".videogen")) {
					videoGens.add(f)
				}
			}
		}

		return videoGens
	}

	def static void main(String[] args) {
		var specificationsDirParent = new File("specifications/")
		var specificationsDir = new ArrayList(specificationsDirParent.listFiles())
		var videoGens = runVideogens(specificationsDir)

		for (File videoGen : videoGens) {
			initMain(videoGen.getName(), videoGen.getParent())
			var parentDir = videoGen.getParent() + "/"
			println(parentDir)
			variants = Variants.calculateVariants(listMan, listOp, listAlt)
			var videos = new ArrayList<String>()
			videos.addAll(listMan)
			videos.addAll(listOp)
			videos.addAll(listAlt)
			CsvTxtGenerator.generateCSV(variants, mapSizes, listMan, listOp, listAlt, parentDir)
			OutPutsGenerator.generateIcons(videos, parentDir)
			var i = 0
			while (i < 10) {
				CsvTxtGenerator.generateVideosSeq(variants, parentDir)
				OutPutsGenerator.generateVideosAndGifs(parentDir, false)
				i++
			}
		}
	}
}
