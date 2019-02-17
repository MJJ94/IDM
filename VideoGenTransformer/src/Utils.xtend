import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Random
import java.util.ArrayList
import java.io.IOException
import java.io.BufferedWriter
import java.io.FileWriter
import java.util.HashMap
import java.io.Reader
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

class Utils {
	def static ArrayList<ArrayList<String>> calculateVariants(ArrayList<String> listMan, ArrayList<String> listOp,
		ArrayList<String> listAlt) {

		var opCombinations = insertOp(listOp)
		var result = new ArrayList<ArrayList<String>>
		var manOpResult = new ArrayList<ArrayList<String>>(insertMan(listMan, opCombinations))

		result = new ArrayList<ArrayList<String>>(insertAlt(listAlt, manOpResult))
		return result;
	}

	def static ArrayList<ArrayList<String>> insertMan(ArrayList<String> listMan,
		ArrayList<ArrayList<String>> opResult) {
		var ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>(opResult)
		for (String man : listMan) {
			for (ArrayList<String> l : result) {
				l.add(man)
			}
		}
		return result
	}

	def static ArrayList<ArrayList<String>> insertOp(ArrayList<String> lOp) {
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

	def static ArrayList<ArrayList<String>> insertAlt(ArrayList<String> listAlt,
		ArrayList<ArrayList<String>> manOpResult) {
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

	def static void generateCSV(ArrayList<ArrayList<String>> variantes, HashMap<String, Long> mapMediaSizes,
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

	def static String toCSV(ArrayList<ArrayList<String>> variantes, HashMap<String, Long> mapMediaSizes,
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

		stringBuilder.append("duration");
		stringBuilder.append(separator);

		stringBuilder.append(separatorLine);

		var id = 1;
		for (ArrayList<String> l : variantes) {
			var long totalSize
			var Double totalDuration = 0.0

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
			stringBuilder.append(separator)

			totalDuration = getDurations(l)
			stringBuilder.append(totalDuration)
			stringBuilder.append(separatorLine)

			id++;
		}
		return stringBuilder.toString();
	}

	def static void generateVideosSeq(ArrayList<ArrayList<String>> variantes) {
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

	def static String toTxt(ArrayList<ArrayList<String>> variantes) {
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

	def static void runCommands() {
		var DateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");
		var Date date = new Date();
		val outPutVideoName = "video_" + df.format(date) + ".mp4"
		val outPutGifName = "gif_" + df.format(date) + ".gif"
		val outPutVideoPath = "outputs/videos/"
		val outPutGifPath = "outputs/gifs/"
		val videoCommand = "ffmpeg -f concat -safe 0 -i videos.txt -c copy " + outPutVideoPath + outPutVideoName
		val playVideoCommand = "vlc " + outPutVideoPath + outPutVideoName
		val gifCommand = "ffmpeg -i " + outPutVideoPath + outPutVideoName + " -r 120 -vf scale=360:-1 " +
			outPutGifPath + outPutGifName + " -hide_banner"
		println("Generating " + outPutVideoPath + outPutVideoName)
		var p = Runtime.runtime.exec(videoCommand)

		if (p.waitFor == 0) {
			println(outPutVideoPath + outPutVideoName + " is generated")
			println("Generating " + outPutGifPath + outPutGifName)
			var gifP = Runtime.runtime.exec(gifCommand)
			if (gifP.waitFor == 0) {
				println(outPutGifPath + outPutGifName + "is generated")
				var vlcP = Runtime.runtime.exec(playVideoCommand)
				vlcP.waitFor == 0
			}
		}
	}

	def static Double getDurations(ArrayList<String> videos) {
		var result = 0.0
		for (String video : videos) {
			val duration = getDuration(video)
			result += duration
		}
		return result
	}

	def static Double getDuration(String videoName) {
		var durationCommand = " ffprobe -i " + videoName + " -show_format"
		var durationP = Runtime.runtime.exec(durationCommand)
		if (durationP.waitFor == 0) {
			try {
				val stdInput = new BufferedReader(new InputStreamReader(durationP.inputStream))
				var c = 0
				while (c > -1) {
					var line = ""
					line = stdInput.readLine.toLowerCase()
					if (line.contains("uration")) {
						val durationStr = line.split("=").get(1)
						val duration = Double.parseDouble(durationStr)
						return duration
					}
					c = stdInput.read
				}
			} catch (IOException e) {
				System.err.println(e)
			}
			return null
		}
	}
}