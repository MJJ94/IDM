import java.util.ArrayList
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.IOException

class Variants {
	def static ArrayList<ArrayList<String>> calculateVariants(ArrayList<String> listMan, ArrayList<String> listOp,
		ArrayList<String> listAlt) {
		var result = new ArrayList<ArrayList<String>>

		var opCombinations = insertOp(listOp)
		var manOpResult = new ArrayList<ArrayList<String>>(insertMan(listMan, opCombinations))
		result = new ArrayList<ArrayList<String>>(insertAlt(listAlt, manOpResult))
		return result;
	}

	def static ArrayList<ArrayList<String>> insertMan(ArrayList<String> listMan,
		ArrayList<ArrayList<String>> opResult) {
		var ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>(opResult)

		if (!listMan.isEmpty()) {
			if (!result.isEmpty()) {
				for (String man : listMan) {
					for (ArrayList<String> l : result) {
						l.add(man)
					}
				}
			} else {
				result.add(listMan)
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

		if (!lOp.isEmpty()) {
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
		}
		return result;
	}

	def static ArrayList<ArrayList<String>> insertAlt(ArrayList<String> listAlt,
		ArrayList<ArrayList<String>> manOpResult) {
		var ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>()

		if (!listAlt.isEmpty()) {
			if (!manOpResult.isEmpty()) {
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
			} else {
				for (String alt : listAlt) {
					var ArrayList<String> l = new ArrayList<String>
					l.add(alt)
					result.add(l)
				}
			}
		} else {
			return manOpResult;
		}

		return result;
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
				return null
			}
		}
	}	
}
