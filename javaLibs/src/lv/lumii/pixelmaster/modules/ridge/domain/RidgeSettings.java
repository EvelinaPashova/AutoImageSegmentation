package lv.lumii.pixelmaster.modules.ridge.domain;


public class RidgeSettings {
    public int smoothingRadius; // izpludināšanas rādius
    public double nonCircularity;      /* = 1.2;// 1 = don'tcare, >1 how much elongated
                                  profile is required <1 saddle points are also allowed */
    public int upperThreshold;   // visas vērtības, kas lielākas par šo ir kores
    public int lowerThreshold;   // visas vērtības, kas mazākas par šo nav kores
    // max attālums starp pikseli un nogriezni, lai tas piederētu nogrieznim.
    //Izmanto Ridge.getLineSegments()
    public double maxGetLinesDistance;
    // maksimālais attālums kādā meklē blakus esošās nogriežņu galapunktus, lai nogriežņus mēģinātu savienot.
    // Izmanto Ridge.concatSegments()
    public double concatRadius;
    // max punkta attālums līdz nogrieznim, lai tas piederētu nogrieznim (nogriežņu apvienošanas gadījumā).
    // Izmanto Ridge.concatSegments()
    public double maxConcatDistance;
    // maksimālais attālums starp diviem nogriežņiem, lai tie tiktu ieskaitīti vienā nogriežņu kopā.
    // Izmanto Ridge.lineGroup()
    public double maxClusterLineDistance;
    // maksimālā attiecība starp nogriežņu veidoto kopu(garākā dimensija) un tās struktūras mērogojuma izmēru.
    // izmanto Symbol.toSymbol()
    public double maxSymbolScale;
    // ja jointSegment = true, tad, meklējot nogriežņus, nākošā nogriežņa sākumpunkts būs iepriekšējā nogriežņa
    // beigu punkts
    //ja jointSegment = false, tad, meklējot nogriežņus, nākošā nogriežņa sākumpunkts būs nākošais punkts
    // aiz iepriekšējā nogriežņa beigu punkta (nosacijums protams darbojas tikai vienas līnijas ietvaros)
    public boolean jointSegments = true;

    public RidgeSettings ( int smoothingRadius, double nonCircularity,
                         int upperThreshold, int lowerThreshold,
                         double maxGetLinesDistance, double maxConcatDistance,
                         double concatRadius, double maxClusterLineDistance,
                         double maxSymbolScale, boolean jointSegments) {
            set ( smoothingRadius, nonCircularity, upperThreshold, lowerThreshold,
                  maxGetLinesDistance,  maxConcatDistance, concatRadius,
                  maxClusterLineDistance, maxSymbolScale, jointSegments);
    }

    //empty constructor will set default values
    public RidgeSettings () {
        this.smoothingRadius = 3;
        this.nonCircularity = 1.2;
        this.upperThreshold = 10;
        this.lowerThreshold = 1;
        this.maxGetLinesDistance = 1;
        this.concatRadius = 3;
        this.maxConcatDistance = 1;
        this.maxClusterLineDistance = 4;
        this.maxSymbolScale = 1.5;
        this.jointSegments = true;
    }

    public boolean set ( int smoothingRadius, double nonCircularity,
                         int upperThreshold, int lowerThreshold,
                         double maxGetLinesDistance, double maxConcatDistance,
                         double concatRadius, double maxClusterLineDistance,
                         double maxSymbolScale, boolean jointSegments) {
  
        assert!( maxGetLinesDistance < 0 || maxConcatDistance <0 || concatRadius <=0 ||
                 maxClusterLineDistance < 0 || maxSymbolScale <=0 ||
                 smoothingRadius < 1 || nonCircularity < 0 || upperThreshold <=0 || lowerThreshold <=0);

        this.smoothingRadius = smoothingRadius;
        this.nonCircularity = nonCircularity;
        this.upperThreshold = upperThreshold;
        this.lowerThreshold = lowerThreshold;
        this.maxGetLinesDistance = maxGetLinesDistance;
        this.concatRadius = maxConcatDistance;
        this.maxConcatDistance = concatRadius;
        this.maxClusterLineDistance = maxClusterLineDistance;
        this.maxSymbolScale = maxSymbolScale;
        this.jointSegments = jointSegments;
        return true;
    }
}

