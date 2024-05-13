#!/bin/bash
#SBATCH -J JobArray
#SBATCH --time=8:00:00     # Walltime
#SBATCH -A uoa00285         # Project Account
#SBATCH --ntasks=1          # number of tasks
#SBATCH --mem-per-cpu=10G  # memory/cpu (in MB)
#SBATCH --cpus-per-task=10   # 10 OpenMP Threads
ml shared
ml Java/1.8.0_144
ml METIS/5.1.0-intel-2017a
ml openblas/dynamic/0.2.18
/nesi/project/uoa00285/AMPL/bin/ampl_lic start
srun java -jar ./dist/BAO_VNS.jar $SLURM_JOBID inputFile_1.txt

