option solver knitro; 
options knitro_options "wantsol=8 	 outlev=1 	 hessopt=6 	 strat_warm_start=1"; 
param R_PTVHD;
set voxelIndex_PTVHD;
set bmltsIndex_PTVHD_150 ;
set voxelbmlt_PTVHD_150 within {voxelIndex_PTVHD,bmltsIndex_PTVHD_150};
param intensities_PTVHD_150 {voxelbmlt_PTVHD_150} >=0;
set bmltsIndex_PTVHD_220 ;
set voxelbmlt_PTVHD_220 within {voxelIndex_PTVHD,bmltsIndex_PTVHD_220};
param intensities_PTVHD_220 {voxelbmlt_PTVHD_220} >=0;
set bmltsIndex_PTVHD_255 ;
set voxelbmlt_PTVHD_255 within {voxelIndex_PTVHD,bmltsIndex_PTVHD_255};
param intensities_PTVHD_255 {voxelbmlt_PTVHD_255} >=0;
set bmltsIndex_PTVHD_295 ;
set voxelbmlt_PTVHD_295 within {voxelIndex_PTVHD,bmltsIndex_PTVHD_295};
param intensities_PTVHD_295 {voxelbmlt_PTVHD_295} >=0;
set bmltsIndex_PTVHD_315 ;
set voxelbmlt_PTVHD_315 within {voxelIndex_PTVHD,bmltsIndex_PTVHD_315};
param intensities_PTVHD_315 {voxelbmlt_PTVHD_315} >=0;
param R_RECTUM;
set voxelIndex_RECTUM;
set bmltsIndex_RECTUM_150 ;
set voxelbmlt_RECTUM_150 within {voxelIndex_RECTUM,bmltsIndex_RECTUM_150};
param intensities_RECTUM_150 {voxelbmlt_RECTUM_150} >=0;
set bmltsIndex_RECTUM_220 ;
set voxelbmlt_RECTUM_220 within {voxelIndex_RECTUM,bmltsIndex_RECTUM_220};
param intensities_RECTUM_220 {voxelbmlt_RECTUM_220} >=0;
set bmltsIndex_RECTUM_255 ;
set voxelbmlt_RECTUM_255 within {voxelIndex_RECTUM,bmltsIndex_RECTUM_255};
param intensities_RECTUM_255 {voxelbmlt_RECTUM_255} >=0;
set bmltsIndex_RECTUM_295 ;
set voxelbmlt_RECTUM_295 within {voxelIndex_RECTUM,bmltsIndex_RECTUM_295};
param intensities_RECTUM_295 {voxelbmlt_RECTUM_295} >=0;
set bmltsIndex_RECTUM_315 ;
set voxelbmlt_RECTUM_315 within {voxelIndex_RECTUM,bmltsIndex_RECTUM_315};
param intensities_RECTUM_315 {voxelbmlt_RECTUM_315} >=0;
param R_BLADDER;
set voxelIndex_BLADDER;
set bmltsIndex_BLADDER_150 ;
set voxelbmlt_BLADDER_150 within {voxelIndex_BLADDER,bmltsIndex_BLADDER_150};
param intensities_BLADDER_150 {voxelbmlt_BLADDER_150} >=0;
set bmltsIndex_BLADDER_220 ;
set voxelbmlt_BLADDER_220 within {voxelIndex_BLADDER,bmltsIndex_BLADDER_220};
param intensities_BLADDER_220 {voxelbmlt_BLADDER_220} >=0;
set bmltsIndex_BLADDER_255 ;
set voxelbmlt_BLADDER_255 within {voxelIndex_BLADDER,bmltsIndex_BLADDER_255};
param intensities_BLADDER_255 {voxelbmlt_BLADDER_255} >=0;
set bmltsIndex_BLADDER_295 ;
set voxelbmlt_BLADDER_295 within {voxelIndex_BLADDER,bmltsIndex_BLADDER_295};
param intensities_BLADDER_295 {voxelbmlt_BLADDER_295} >=0;
set bmltsIndex_BLADDER_315 ;
set voxelbmlt_BLADDER_315 within {voxelIndex_BLADDER,bmltsIndex_BLADDER_315};
param intensities_BLADDER_315 {voxelbmlt_BLADDER_315} >=0;
param bmlt150;
param bmlt220;
param bmlt255;
param bmlt295;
param bmlt315;
param totalbmlt; 	#number of beamlets 
param UB1;
param UB2;
param UB3;
param t;
param epsilon;
param OAR_targetUB;
var x150 {1 .. 68} >= 0, <=30.0, default 1; 
var x220 {1 .. 70} >= 0, <=30.0, default 1; 
var x255 {1 .. 63} >= 0, <=30.0, default 1; 
var x295 {1 .. 64} >= 0, <=30.0, default 1; 
var x315 {1 .. 65} >= 0, <=30.0, default 1; 
param a{1 .. 4}; 
param v{1 .. 4}; 
param EUD0{1 .. 4}; 
var intensityVoxel_PTVHD{1 .. 15172}; 
var intensityVoxel_RECTUM{1 .. 18128}; 
var intensityVoxel_BLADDER{1 .. 22936}; 
minimize Total_Cost: - log((1+(( ((1/R_RECTUM)*(sum {i in voxelIndex_RECTUM} (
(sum{j in bmltsIndex_RECTUM_150: (i,j) in voxelbmlt_RECTUM_150} x150[j]*intensities_RECTUM_150[i,j]) 
+(sum{j in bmltsIndex_RECTUM_220: (i,j) in voxelbmlt_RECTUM_220} x220[j]*intensities_RECTUM_220[i,j]) 
+(sum{j in bmltsIndex_RECTUM_255: (i,j) in voxelbmlt_RECTUM_255} x255[j]*intensities_RECTUM_255[i,j]) 
+(sum{j in bmltsIndex_RECTUM_295: (i,j) in voxelbmlt_RECTUM_295} x295[j]*intensities_RECTUM_295[i,j]) 
+(sum{j in bmltsIndex_RECTUM_315: (i,j) in voxelbmlt_RECTUM_315} x315[j]*intensities_RECTUM_315[i,j]) 
)^a[2])) )^(1/a[2])/EUD0[2])^v[2])^-1)- log((1+(( ((1/R_BLADDER)*(sum {i in voxelIndex_BLADDER} (
(sum{j in bmltsIndex_BLADDER_150: (i,j) in voxelbmlt_BLADDER_150} x150[j]*intensities_BLADDER_150[i,j]) 
+(sum{j in bmltsIndex_BLADDER_220: (i,j) in voxelbmlt_BLADDER_220} x220[j]*intensities_BLADDER_220[i,j]) 
+(sum{j in bmltsIndex_BLADDER_255: (i,j) in voxelbmlt_BLADDER_255} x255[j]*intensities_BLADDER_255[i,j]) 
+(sum{j in bmltsIndex_BLADDER_295: (i,j) in voxelbmlt_BLADDER_295} x295[j]*intensities_BLADDER_295[i,j]) 
+(sum{j in bmltsIndex_BLADDER_315: (i,j) in voxelbmlt_BLADDER_315} x315[j]*intensities_BLADDER_315[i,j]) 
)^a[3])) )^(1/a[3])/EUD0[3])^v[3])^-1);
 s.t. 
equalityTarget: 	( ((1/R_PTVHD)*(sum {i in voxelIndex_PTVHD}( 
(sum {j in bmltsIndex_PTVHD_150: (i,j) in voxelbmlt_PTVHD_150} x150[j]*intensities_PTVHD_150[i,j]) 
+(sum {j in bmltsIndex_PTVHD_220: (i,j) in voxelbmlt_PTVHD_220} x220[j]*intensities_PTVHD_220[i,j]) 
+(sum {j in bmltsIndex_PTVHD_255: (i,j) in voxelbmlt_PTVHD_255} x255[j]*intensities_PTVHD_255[i,j]) 
+(sum {j in bmltsIndex_PTVHD_295: (i,j) in voxelbmlt_PTVHD_295} x295[j]*intensities_PTVHD_295[i,j]) 
+(sum {j in bmltsIndex_PTVHD_315: (i,j) in voxelbmlt_PTVHD_315} x315[j]*intensities_PTVHD_315[i,j]) 
)^a[1])))^(1/a[1]) >= t; 
constraintOAR_Target: 	(((1/R_PTVHD)*(sum {i in voxelIndex_PTVHD} ( 
 (sum {j in bmltsIndex_PTVHD_150: (i,j) in voxelbmlt_PTVHD_150} x150[j]*intensities_PTVHD_150[i,j]) 
+(sum {j in bmltsIndex_PTVHD_220: (i,j) in voxelbmlt_PTVHD_220} x220[j]*intensities_PTVHD_220[i,j]) 
+(sum {j in bmltsIndex_PTVHD_255: (i,j) in voxelbmlt_PTVHD_255} x255[j]*intensities_PTVHD_255[i,j]) 
+(sum {j in bmltsIndex_PTVHD_295: (i,j) in voxelbmlt_PTVHD_295} x295[j]*intensities_PTVHD_295[i,j]) 
+(sum {j in bmltsIndex_PTVHD_315: (i,j) in voxelbmlt_PTVHD_315} x315[j]*intensities_PTVHD_315[i,j]) 
)^a[4])))^(1/a[4]) <=OAR_targetUB;
