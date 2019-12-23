package mapElements.animals;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

public class Genotype {

    public final ArrayList<Integer> genes;
    private int genotypeSize = 32;
    private static final int genesTypes = 8;
    private int domineeringGene;

    public Genotype() {
        Random r = new Random();

        genes = new ArrayList<>();

        for (int i = 0; i < genotypeSize; i++) genes.add(r.nextInt(genesTypes));
        checkGenes(r);

        Collections.sort(genes);
        this.domineeringGene = computeDomineeringGene();

    }

    public Genotype( Genotype parent1, Genotype parent2) {
        Random r = new Random();
        int rightBound = r.nextInt(genotypeSize - 2) + 2;
        int leftBound = r.nextInt(rightBound-1) + 1;

        genes = new ArrayList<>();
        genes.addAll(parent1.genes.subList(0,leftBound));
        genes.addAll(parent2.genes.subList(leftBound,rightBound));
        genes.addAll(parent1.genes.subList(rightBound,parent1.genes.size()));

        checkGenes(r);

        Collections.sort(genes);

        this.domineeringGene = computeDomineeringGene();


    }

    private int computeDomineeringGene () {
        int [] array = new int[genesTypes];
        genes.forEach(x -> array[x]++);

        int max = -1, maxIndex = -1;
        for (int i = 0; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
                maxIndex = i;
            }
        }

        return maxIndex;
    }
    private void checkGenes(Random rand) {
        for (int i = 0 ; i < genesTypes; i++) {
            if (!this.genes.contains(i)) {
                while (true) {
                    int position = rand.nextInt(this.genotypeSize);
                    if (this.genes.indexOf(this.genes.get(position)) != this.genes.lastIndexOf(this.genes.get(position))) {
                        this.genes.set(position,i);
                        break;
                    }

                }
            }
        }
    }


    public int getDomineeringGene()
    {
        return this.domineeringGene;
    }


    public int pickDirection() {
        Random r = new Random();
        return  this.genes.get(r.nextInt(this.genotypeSize));
    }

    @Override
    public String toString() {
        return this.genes.toString();
    }
}
