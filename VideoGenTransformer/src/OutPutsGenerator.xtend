import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.ArrayList
import java.io.IOException
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

class OutPutsGenerator {

	def static void generateVideosAndGifs(String parentDir, boolean playVideo) {
		var DateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");
		var Date date = new Date();
		val outPutVideoName = "video_" + df.format(date) + ".mp4"
		val outPutGifName = "gif_" + df.format(date) + ".gif"
		var outPutsPath = parentDir + "outputs"
		try {
			Files.createDirectories(Paths.get(outPutsPath))
		} catch (Exception e) {
			System.err.println(e)
		}
		val outPutVideoPath = outPutsPath + "/videos"
		val outPutGifPath = outPutsPath + "/gifs"
		try {
			Files.createDirectories(Paths.get(outPutVideoPath))
			Files.createDirectories(Paths.get(outPutGifPath))
		} catch (Exception e) {
			System.err.println(e)
		}

		val videoCommand = "ffmpeg -f concat -safe 0 -i " + parentDir + "videos.txt -c copy " + outPutVideoPath + "/" +
			outPutVideoName
		val playVideoCommand = "vlc " + outPutVideoPath + "/" + outPutVideoName
		val gifCommand = "ffmpeg -i " + outPutVideoPath + "/" + outPutVideoName + " -r 120 -vf scale=360:-1 " +
			outPutGifPath + "/" + outPutGifName + " -hide_banner"
		println("Generating " + outPutVideoPath + "/" + outPutVideoName)
		var p = Runtime.runtime.exec(videoCommand)

		if (p.waitFor == 0) {
			println(outPutVideoPath + outPutVideoName + " is generated")
			println()
			println("Generating " + outPutGifPath + "/" + outPutGifName)
			var gifP = Runtime.runtime.exec(gifCommand)
			if (gifP.waitFor == 0) {
				println(outPutGifPath + "/" + outPutGifName + "is generated")
				println()
				if (playVideo) {
					var vlcP = Runtime.runtime.exec(playVideoCommand)
					vlcP.waitFor == 0
				}
			}
		} else {
			try {
				val stdInput = new BufferedReader(new InputStreamReader(p.errorStream))
				var c = 0
				var ArrayList<String> lines = new ArrayList<String>
				while (c > -1) {
					var line = ""
					line = stdInput.readLine()
					println(line)
					lines.add(line)
					c = stdInput.read
				}
				return
			} catch (IOException e) {
				System.err.println(e)
			}
		}
	}

	def static void generateIcons(ArrayList<String> videos, String parentDir) {
		var id = 0
		var outPutsPath = parentDir + "outputs"
		try {
			Files.createDirectories(Paths.get(outPutsPath))
		} catch (Exception e) {
			System.err.println(e)
		}

		var iconsDirPath = outPutsPath + "/icons"
		try {
			Files.createDirectories(Paths.get(iconsDirPath))
		} catch (Exception e) {
			System.err.println(e)
		}

		for (String video : videos) {
			var iconName = "icon_" + id + ".png"
			var iconPath = iconsDirPath + "/" + iconName
			var duration = Variants.getDuration(parentDir + video)
			val start = duration / 2
			val end = start + 1
			val command = "ffmpeg -y -i " + parentDir + video + " -r 1 -t " + start + " -ss " + end + " -vframes 1 " +
				iconPath
			val p = Runtime.runtime.exec(command)
			if (p.waitFor != 0) {
				try {
					val stdInput = new BufferedReader(new InputStreamReader(p.errorStream))
					var c = 0
					while (c > -1) {
						var line = ""
						line = stdInput.readLine()
						println(line)
						c = stdInput.read
					}
					return
				} catch (IOException e) {
					System.err.println(e)
				}
			} else {
				println(video + " icon is generated with name of " + iconName)
			}
			id++
		}
	}
}
