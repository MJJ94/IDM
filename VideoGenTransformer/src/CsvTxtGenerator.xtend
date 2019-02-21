import java.util.ArrayList
import java.util.HashMap
import java.text.DateFormat
import java.util.Date
import java.io.BufferedWriter
import java.text.SimpleDateFormat
import java.io.FileWriter
import java.io.IOException
import java.util.Random
import java.nio.file.Files
import java.nio.file.Paths

class CsvTxtGenerator {
	def static String generateCSV(ArrayList<ArrayList<String>> variantes, HashMap<String, Long> mapMediaSizes,
		ArrayList<String> listMan, ArrayList<String> listOpt, ArrayList<String> listAlt, String parentDir) {
		var DateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");
		var Date date = new Date();
		var BufferedWriter writer;
		val fileName = "result-" + df.format(date) + ".csv"
		val resultsDirPath = parentDir + "results"
		try {
			Files.createDirectories(Paths.get(resultsDirPath))
		} catch (Exception e) {
			System.err.println(e)
		}
		val filePath = resultsDirPath + "/" + fileName
		try {
			writer = new BufferedWriter(new FileWriter(filePath))
			writer.write(toCSV(variantes, mapMediaSizes, listMan, listOpt, listAlt, parentDir));
			writer.flush()
			writer.close()
			return filePath
		} catch (IOException exception) {
			System.err.println(exception)
			return null
		}
	}

	def static String toCSV(ArrayList<ArrayList<String>> variantes, HashMap<String, Long> mapMediaSizes,
		ArrayList<String> listMan, ArrayList<String> listOpt, ArrayList<String> listAlt, String parentDir) {
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

			totalDuration = Variants.getDurations(l, parentDir)
			stringBuilder.append(totalDuration)
			stringBuilder.append(separatorLine)

			id++;
		}
		return stringBuilder.toString();
	}

	def static void generateVideosSeq(ArrayList<ArrayList<String>> variantes, String parentDir) {
		var BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter( parentDir + "videos" + ".txt"))
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
}
