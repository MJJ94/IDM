import org.junit.Test
import org.eclipse.emf.common.util.URI

import static org.junit.Assert.*
import fr.istic.videoGen.OptionalMedia
import fr.istic.videoGen.Media
import fr.istic.videoGen.MandatoryMedia
import fr.istic.videoGen.VideoDescription
import fr.istic.videoGen.VideoText
import java.io.File
import fr.istic.videoGen.AlternativesMedia
import fr.istic.videoGen.MediaDescription
import java.util.ArrayList
import java.util.Stack
import java.util.HashSet
import java.util.HashMap
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.io.BufferedWriter
import java.util.Date
import java.io.FileWriter
import java.io.IOException

class VideoGenTest1XtendVersion {

	@Test
	def void testLoadModel() {
		val listMan = new ArrayList<String>();
		val listOp = new ArrayList<String>();
		val listAlt = new ArrayList<String>();
		var mapSizes = new HashMap<String, Long>

		val videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI("specification.videogen"))
		assertNotNull(videoGen)
		for (Media m : videoGen.medias) {
			if (m instanceof MandatoryMedia) {
				val man = m as MandatoryMedia
				if (man.description instanceof VideoDescription) {
					val des = man.description as VideoDescription
					val f = new File(des.location)
					println(des.location + " " + f.length)
					listMan.add(des.location);
					mapSizes.put(des.location, f.length)
				}
			} else if (m instanceof OptionalMedia) {
				val op = m as OptionalMedia
				if (op.description instanceof VideoDescription) {
					val des = op.description as VideoDescription
					val f = new File(des.location)
					println(des.location + " " + f.length)
					listOp.add(des.location);
					mapSizes.put(des.location, f.length)
				}
			} else if (m instanceof AlternativesMedia) {
				val alt = m as AlternativesMedia
				for (MediaDescription malt : alt.medias) {
					if (malt instanceof VideoDescription) {
						val des = malt as VideoDescription
						val f = new File(des.location)
						println(des.location + " " + f.length)
						listAlt.add(des.location)
						mapSizes.put(des.location, f.length)
					}
				}
			}
		}
		val result = calculateVariants(listMan, listOp, listAlt)
		generateCSV(result, mapSizes, listMan, listOp, listAlt)
//		println(result.size())
	}

	def ArrayList<ArrayList<String>> calculateVariants(ArrayList<String> listMan, ArrayList<String> listOp,
		ArrayList<String> listAlt) {
		println("listOp.size() = " + listOp.size() + " listAlt.size() = " + listAlt.size())

		var opCombinations = insertOp(listOp)
		var result = new ArrayList<ArrayList<String>>
		var j = 0;
		var i = 0;
		for (ArrayList<String> l : opCombinations) {
			for (String alt : listAlt) {
				result.add(l)
			}
		}

		for (ArrayList<String> l : result) {
			for (String man : listMan) {
				l.add(man)
			}
		}
		while (j < result.size()) {
			for (String alt : listAlt) {
				result.get(j).add(alt);
				j++;
			}
			i++;
		}

		return result;
	}

	def ArrayList<ArrayList<Media>> initResult(ArrayList<MandatoryMedia> listMan, int size) {

		val result = new ArrayList<ArrayList<Media>>();
		var i = 0;
		while (i < size) {
			result.add(new ArrayList<Media>(listMan))
			i++;
		}

		return result;
	}

	def ArrayList<ArrayList<String>> insertOp(ArrayList<String> lOp) {
		var size = (Math.pow(2, lOp.size())).intValue;
		var sizePreced = size / 2;
		var result = new ArrayList<ArrayList<String>>
		var i = 0;
		var j = 1;
		var initSizePreced = sizePreced
		var initI = 0;
		while (i < size) {
			result.add(new ArrayList<String>)
			i++;
		}
		i = 0;
		for (String elem : lOp) {
			i = 0;
			initI = i
			sizePreced = size / (j * 2)
			initSizePreced = sizePreced
			println(elem + " " + sizePreced)
			while (i < size && sizePreced < size) {
				while (i < sizePreced) {
					result.get(i).add(elem)
					i++;
				}
				initI += (initSizePreced * 2)
				i = initI
				sizePreced = (initSizePreced * 2) + sizePreced
			}
			j *= 2
		}
		return result;
	}

	def void generateCSV(ArrayList<ArrayList<String>> variantes, HashMap<String, Long> mapMediaSizes,
		ArrayList<String> listMan, ArrayList<String> listOpt, ArrayList<String> listAlt) {
		var DateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");
		var Date date = new Date();
		var BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter("./results/result-" + df.format(date) + ".csv"))
			writer.write(toCSV(variantes, mapMediaSizes, listMan, listOpt, listAlt));
			writer.flush()
			writer.close()
		} catch (IOException exception) {
			System.err.println(exception)
		}

	}

	def String toCSV(ArrayList<ArrayList<String>> variantes, HashMap<String, Long> mapMediaSizes,
		ArrayList<String> listMan, ArrayList<String> listOpt, ArrayList<String> listAlt) {
		var String separator = ";";
		var String separatorLine = "\n";
		var StringBuilder stringBuilder = new StringBuilder();
		var ArrayList<String> columnNames = new ArrayList
		// Adding CSV Headers
		stringBuilder.append("id");
		stringBuilder.append(separator);
		for (String man : listMan) {
			stringBuilder.append(man);
			columnNames.add(man)
			stringBuilder.append(separator);
		}
		for (String opt : listOpt) {
			stringBuilder.append(opt);
			columnNames.add(opt)
			stringBuilder.append(separator);
		}

		for (String alt : listAlt) {
			stringBuilder.append(alt);
			columnNames.add(alt)
			stringBuilder.append(separator);
		}

		stringBuilder.append("size");
		stringBuilder.append(separator);

		stringBuilder.append(separatorLine);

		var id = 1;
		for (ArrayList<String> l : variantes) {
			var long totalSize
			stringBuilder.append(id)
			stringBuilder.append(separator)
			for (String column : columnNames) {
				stringBuilder.append(l.contains(column))
				stringBuilder.append(separator)
			}
			for(String v: l){
				var Long size = mapMediaSizes.get(v)
				totalSize += size
			}
			stringBuilder.append(totalSize)
			stringBuilder.append(separatorLine)
			id++;
		}
		println(stringBuilder.toString())
		return stringBuilder.toString();
	}
}
