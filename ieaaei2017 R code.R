# Execute:
link <- "http://files.grouplens.org/datasets/movielens/ml-1m.zip"

doc <- basename(link)

download.file(link, doc)

unzip(doc)

install.packages("readr")
library(readr)
ratings1ml <- read_delim("ml-1m/ratings.dat","::", escape_double = FALSE, col_names = FALSE)
# Remove data not useful for the experiment.
ratings1ml <- ratings1ml[,-c(2,4,6,7)]

install.packages("Matrix")
library(Matrix)


ML1M <- sparseMatrix(i = ratings1ml$X1, j = ratings1ml$X3, x = ratings1ml$X5)

ML1M <- as.matrix(ML1M)

#rrecsys####
install.packages("devtools")

library(devtools)

install_github("ludovikcoba/rrecsys")
library(rrecsys)


RM <- defineData(ML1M)
e <- evalModel(RM)

#Weighted Slope One rrecsys####

evalPred(e, "slope")

#FunkSVD rrecsys####

# Change iter, k, gamma and lambda to replicate according to the value on the pdf.
iter <- 10; k <- 20; g <- 0.03; l <- 0.002

setStoppingCriteria(nrLoops = iter)

evalPred(e, "funk", k, g,l)

detach("package:rrecsys", unload = T)



