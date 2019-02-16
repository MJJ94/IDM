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
import java.util.HashMap
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.io.BufferedWriter
import java.util.Date
import java.io.FileWriter
import java.io.IOException
import java.util.Random

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
		generateVideosSeq(result)
		runCommands();
	}

	def ArrayList<ArrayList<String>> calculateVariants(ArrayList<String> listMan, ArrayList<String> listOp,
		ArrayList<String> listAlt) {
		println("listOp.size() = " + listOp.size() + " listAlt.size() = " + listAlt.size())

		var opCombinations = insertOp(listOp)
		var result = new ArrayList<ArrayList<String>>

		var manOpResult = new ArrayList<ArrayList<String>>(insertMan(listMan, opCombinations))

		result = new ArrayList<ArrayList<String>>(insertAlt(listAlt, manOpResult))
		return result;
	}

	def ArrayList<ArrayList<String>> insertMan(ArrayList<String> listMan, ArrayList<ArrayList<String>> opResult) {
		var ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>(opResult)
		for (String man : listMan) {
			for (ArrayList<String> l : result) {
				l.add(man)
			}
		}
		return result
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

	def ArrayList<ArrayList<String>> insertAlt(ArrayList<String> listAlt, ArrayList<ArrayList<String>> manOpResult) {
		var ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>()
		var j = 0;
		for (ArrayList<String> l : manOpResult) {
			for (String alt : listAlt) {
				result.add(new ArrayList(l))
			}
		}
		var size = result.size()

		while (j < size) {
			for (String alt : listAlt) {
				result.get(j).add(alt);
				j++;
			}
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
			for (String v : l) {
				var Long size = mapMediaSizes.get(v)
				totalSize += size
			}
			stringBuilder.append(totalSize)
			stringBuilder.append(separatorLine)
			id++;
		}
		return stringBuilder.toString();
	}

	def void generateVideosSeq(ArrayList<ArrayList<String>> variantes) {
		var BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter("./videos" + ".txt"))
			writer.write(toTxt(variantes));
			writer.flush()
			writer.close()
		} catch (IOException exception) {
			System.err.println(exception)
		}
	}

	def String toTxt(ArrayList<ArrayList<String>> variantes) {
		var String separatorLine = "\n";
		var StringBuilder stringBuilder = new StringBuilder();
		var Random random = new Random()
		var randomIndex = random.nextInt(variantes.size - 1)
		var randomVar = variantes.get(randomIndex)
		for (String elem : randomVar) {
			stringBuilder.append("file '" + elem + "'");
			stringBuilder.append(separatorLine)
		}

		return stringBuilder.toString()
	}

	def void runCommands() {
		var command = "ffmpeg -f concat -safe 0 -i videos.txt -c copy output.mp4"
		var playVideoCommand = "vlc output.mp4"
		var remove = "rm output.mp4"
		var DateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");
		var Date date = new Date();
		var gifCommand = "ffmpeg -i output.mp4 gif_" + df.format(date) + ".gif -hide_banner"
		var p = Runtime.runtime.exec(command)

		if (p.waitFor == 0) {
			var gifP = Runtime.runtime.exec(gifCommand)
			if (gifP.waitFor == 0) {
				var vlcP = Runtime.runtime.exec(playVideoCommand)
				if (vlcP.waitFor == 0) {
					var removeP = Runtime.runtime.exec(remove)
					removeP.waitFor
				}
			}
		}
	}
}
}
