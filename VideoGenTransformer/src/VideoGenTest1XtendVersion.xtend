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

class VideoGenTest1XtendVersion {

	@Test
	def void testLoadModel() {
		val listMan = new ArrayList<MandatoryMedia>();
		val listOp = new ArrayList<OptionalMedia>();
		val listAlt = new ArrayList<AlternativesMedia>();
		val videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI("specification.videogen"))
		assertNotNull(videoGen)
		for (Media m : videoGen.medias) {
			if (m instanceof MandatoryMedia) {
				val man = m as MandatoryMedia
				if (man.description instanceof VideoDescription) {
					val des = man.description as VideoDescription
					val f = new File(des.location)
					println(des.location + " " + f.length)
					listMan.add(man);
				}
			} else if (m instanceof OptionalMedia) {
				val op = m as OptionalMedia
				if (op.description instanceof VideoDescription) {
					val des = op.description as VideoDescription
					val f = new File(des.location)
					println(des.location + " " + f.length)
					listOp.add(op);
				}
			} else if (m instanceof AlternativesMedia) {
				val alt = m as AlternativesMedia
				for (MediaDescription malt : alt.medias) {
					if (malt instanceof VideoDescription) {
						val des = malt as VideoDescription
						val f = new File(des.location)
						println(des.location + " " + f.length)
						listAlt.add(alt)
					}
				}
			}
		}
		val result = calculateVariants(listMan, listOp, listAlt)
//		println(result.size())
	}

	def ArrayList calculateVariants(ArrayList<MandatoryMedia> listMan, ArrayList<OptionalMedia> listOp,
		ArrayList<AlternativesMedia> listAlt) {
		println("listOp.size() = " + listOp.size() + " listAlt.size() = " + listAlt.size())

		val resultNumber = (listOp.size() * 2) * listAlt.size();
		var result = initResult(listMan, resultNumber)
		var optionelCombinations = new ArrayList<ArrayList<String>>();
//		recursive_combinations_start(new ArrayList<String>, optionelCombinations);
		var j = 0;
		var i = 0;
		println("size = " + result.size())
		recursive_combinations_start()
		while (j < resultNumber) {
			for (AlternativesMedia alt : listAlt) {
				result.get(j).add(alt);
				if (i < optionelCombinations.size()) {
					println(optionelCombinations.get(i).size())
				}
//				println(result.get(j).size())
				j++;
//				println("j " + j)
//				println(i)
			}
			i++;
		}

		return result;
	}

	def boolean contain(ArrayList<ArrayList<OptionalMedia>> used, ArrayList<OptionalMedia> combo) {
		var boolean compar = false;
		for (ArrayList<OptionalMedia> l : used) {
			println("l: " + l + " combo: " + combo)
			compar = ((new HashSet<OptionalMedia>(l)).equals((new HashSet<OptionalMedia>(combo))));
			if (compar) {
				return true;
			}
		}
		return false;
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

	def void recursive_combinations(ArrayList<String> combination, int ndx, ArrayList<String> elems,
		ArrayList<ArrayList<String>> result) {
		if (ndx == elems.length) { // (reached end of list after selecting/not selecting) 
			result.add(combination)
		} else { // (include element at ndx) 
			combination.add(elems.get(ndx));
			recursive_combinations(combination, ndx + 1, elems, result);
			// (don't include element at ndx) 
			combination.remove(elems.get(ndx));
			recursive_combinations(combination, ndx + 1, elems, result);
		}
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
			sizePreced = size/(j*2)
			initSizePreced = sizePreced
			println(elem + " " + sizePreced)
			while (i < size && sizePreced < size) {
				while (i < sizePreced) {
					result.get(i).add(elem)
					i++;
				}
				initI += (initSizePreced * 2)
				i = initI
				println("i = " + i)
				sizePreced = (initSizePreced * 2) + sizePreced   
				println("sizePreced = " + sizePreced)
			}
			j *= 2
		}
		return result;
	}

	def void recursive_combinations_start() {
		val ArrayList<String> combination = new ArrayList<String>();
		combination.add("A")
		combination.add("B")
		combination.add("C")
		combination.add("D")
		val result = insertOp(combination)
		for (ArrayList<String> l : result) {
			println("l:" + l)
		}
	}
}
