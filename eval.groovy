import org.grouplens.lenskit.*
import org.grouplens.lenskit.iterative.*
import org.grouplens.lenskit.mf.funksvd.*
import org.grouplens.lenskit.transform.normalize.*
import org.grouplens.lenskit.knn.*
import org.grouplens.lenskit.knn.item.*
import org.grouplens.lenskit.vectors.similarity.*
import org.grouplens.lenskit.eval.data.crossfold.*
import  org.grouplens.lenskit.data.pref.*
import org.grouplens.lenskit.slopeone.*
import org.grouplens.lenskit.eval.data.crossfold.CrossfoldTask.*



trainTest {
    dataset crossfold("ml-1m") {
        source csvfile("ml-1m/ratings.dat") {
            delimiter "::"
            domain {
                minimum 1
                maximum 5.0
				precision 1
            }
        }
		
   
        splitUsers false                       
		partitions 5

    }
	
    algorithm("FunkSVD") {
        bind ItemScorer to FunkSVDItemScorer
        
        bind (BaselineScorer, ItemScorer) to UserMeanItemScorer
        bind (UserMeanBaseline, ItemScorer) to ItemMeanRatingItemScorer


        // change according to pdf
        set FeatureCount to 10
		set InitialFeatureValue to 0.1
        // learning rate corresponds to lambda
        set LearningRate to 0.002
        // ragularization term in the pdf is gamma
		set RegularizationTerm to 0.03
        
        set IterationCount to 10


    }

    algorithm("slopeone"){

        bind ItemScorer to WeightedSlopeOneItemScorer
        bind (BaselineScorer,ItemScorer) to UserMeanItemScorer
        bind (UserMeanBaseline,ItemScorer) to ItemMeanRatingItemScorer
        set DeviationDamping to 0.0d

    }   
    metric RMSEPredictMetric
    metric MAEPredictMetric 

    output "eval-results.csv"
}
