calculateStatsForData <- function (rawData) {
  calculatedStats <- list("id" = c(), "median" = c(), "mad" = c(), "iqr" = c())
  for (columnName in names(rawData)) {
    calculatedStats[["id"]] <- c(calculatedStats[["id"]], columnName)
    cData <- rawData[[columnName]]
    sMedian <- median(cData)
    calculatedStats[["median"]] <- c(calculatedStats[["median"]], sMedian)
    sMad <- mad(cData)
    calculatedStats[["mad"]] <- c(calculatedStats[["mad"]], sMad)
    sIqr <- IQR(cData)
    calculatedStats[["iqr"]] <- c(calculatedStats[["iqr"]], sIqr)
  }
  calculatedStats
}

files <- c("combined-resolution.csv", "deep-resolution.csv", "model-load.csv", "no-proxy-resolution.csv", "unresolvable.csv", "unresolvable-uri.csv")
for (fileName in files) {
  rawData <- read.csv(fileName)
  calculatedStats <- calculateStatsForData(rawData)
  write.csv(calculatedStats, paste("stats-", fileName, sep = ""))
}

#l <- length(calculatedStats[["id"]])
#for (i in 1:(l - 1)) {
  #for (j in (i + 1):l) {
    #idX <- calculatedStats[["id"]][i]
    #idY <- calculatedStats[["id"]][j]
    #rX <- rawData[[idX]]
    #rY <- rawData[[idY]]
    #nX <- length(rX)
    #nY <- length(rY)
    #sMadX <- calculatedStats[["mad"]][i]
    #sMadY <- calculatedStats[["mad"]][j]
    #pMad <- sqrt(((nX - 1) * sMadX * sMadX + (nY - 1) * sMadY * sMadY) / (nX + nY - 2))
    #x <- c()
    #y <- c()
    #for (q in 20:80) {
      #x <- c(x, q)
      #sQx <- quantile(rX, probs = c(q / 100))
      #sQy <- quantile(rY, probs = c(q / 100))
      #y <- c(y, (sQy - sQx) / pMad)
    #}
    #plot(x, y, main = paste(idX, idY))
  #}
#}
