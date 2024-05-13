option solver knitro; 
options knitro_options "wantsol=8 	 outlev=1 	 hessopt=6 	 strat_warm_start=1"; 
param R_PTVHD;
set voxelIndex_PTVHD;
set bmltsIndex_PTVHD_5 ;
set voxelbmlt_PTVHD_5 within {voxelIndex_PTVHD,bmltsIndex_PTVHD_5};
param intensities_PTVHD_5 {voxelbmlt_PTVHD_5} >=0;
set bmltsIndex_PTVHD_105 ;
set voxelbmlt_PTVHD_105 within {voxelIndex_PTVHD,bmltsIndex_PTVHD_105};
param intensities_PTVHD_105 {voxelbmlt_PTVHD_105} >=0;
set bmltsIndex_PTVHD_185 ;
set voxelbmlt_PTVHD_185 within {voxelIndex_PTVHD,bmltsIndex_PTVHD_185};
param intensities_PTVHD_185 {voxelbmlt_PTVHD_185} >=0;
set bmltsIndex_PTVHD_230 ;
set voxelbmlt_PTVHD_230 within {voxelIndex_PTVHD,bmltsIndex_PTVHD_230};
param intensities_PTVHD_230 {voxelbmlt_PTVHD_230} >=0;
set bmltsIndex_PTVHD_310 ;
set voxelbmlt_PTVHD_310 within {voxelIndex_PTVHD,bmltsIndex_PTVHD_310};
param intensities_PTVHD_310 {voxelbmlt_PTVHD_310} >=0;
param R_RECTUM;
set voxelIndex_RECTUM;
set bmltsIndex_RECTUM_5 ;
set voxelbmlt_RECTUM_5 within {voxelIndex_RECTUM,bmltsIndex_RECTUM_5};
param intensities_RECTUM_5 {voxelbmlt_RECTUM_5} >=0;
set bmltsIndex_RECTUM_105 ;
set voxelbmlt_RECTUM_105 within {voxelIndex_RECTUM,bmltsIndex_RECTUM_105};
param intensities_RECTUM_105 {voxelbmlt_RECTUM_105} >=0;
set bmltsIndex_RECTUM_185 ;
set voxelbmlt_RECTUM_185 within {voxelIndex_RECTUM,bmltsIndex_RECTUM_185};
param intensities_RECTUM_185 {voxelbmlt_RECTUM_185} >=0;
set bmltsIndex_RECTUM_230 ;
set voxelbmlt_RECTUM_230 within {voxelIndex_RECTUM,bmltsIndex_RECTUM_230};
param intensities_RECTUM_230 {voxelbmlt_RECTUM_230} >=0;
set bmltsIndex_RECTUM_310 ;
set voxelbmlt_RECTUM_310 within {voxelIndex_RECTUM,bmltsIndex_RECTUM_310};
param intensities_RECTUM_310 {voxelbmlt_RECTUM_310} >=0;
param R_BLADDER;
set voxelIndex_BLADDER;
set bmltsIndex_BLADDER_5 ;
set voxelbmlt_BLADDER_5 within {voxelIndex_BLADDER,bmltsIndex_BLADDER_5};
param intensities_BLADDER_5 {voxelbmlt_BLADDER_5} >=0;
set bmltsIndex_BLADDER_105 ;
set voxelbmlt_BLADDER_105 within {voxelIndex_BLADDER,bmltsIndex_BLADDER_105};
param intensities_BLADDER_105 {voxelbmlt_BLADDER_105} >=0;
set bmltsIndex_BLADDER_185 ;
set voxelbmlt_BLADDER_185 within {voxelIndex_BLADDER,bmltsIndex_BLADDER_185};
param intensities_BLADDER_185 {voxelbmlt_BLADDER_185} >=0;
set bmltsIndex_BLADDER_230 ;
set voxelbmlt_BLADDER_230 within {voxelIndex_BLADDER,bmltsIndex_BLADDER_230};
param intensities_BLADDER_230 {voxelbmlt_BLADDER_230} >=0;
set bmltsIndex_BLADDER_310 ;
set voxelbmlt_BLADDER_310 within {voxelIndex_BLADDER,bmltsIndex_BLADDER_310};
param intensities_BLADDER_310 {voxelbmlt_BLADDER_310} >=0;
param bmlt5;
param bmlt105;
param bmlt185;
param bmlt230;
param bmlt310;
param totalbmlt; 	#number of beamlets 
param UB1;
param UB2;
param UB3;
param t;
param epsilon;
param OAR_targetUB;
var x5 {1 .. 70} >= 0, <=30.0, default 1; 
var x105 {1 .. 63} >= 0, <=30.0, default 1; 
var x185 {1 .. 71} >= 0, <=30.0, default 1; 
var x230 {1 .. 69} >= 0, <=30.0, default 1; 
var x310 {1 .. 67} >= 0, <=30.0, default 1; 
param a{1 .. 4}; 
param v{1 .. 4}; 
param EUD0{1 .. 4}; 
var intensityVoxel_PTVHD{1 .. 15172}; 
var intensityVoxel_RECTUM{1 .. 18128}; 
var intensityVoxel_BLADDER{1 .. 22936}; 
minimize Total_Cost: - log((1+(( ((1/R_RECTUM)*(sum {i in voxelIndex_RECTUM} (
(sum{j in bmltsIndex_RECTUM_5: (i,j) in voxelbmlt_RECTUM_5} x5[j]*intensities_RECTUM_5[i,j]) 
+(sum{j in bmltsIndex_RECTUM_105: (i,j) in voxelbmlt_RECTUM_105} x105[j]*intensities_RECTUM_105[i,j]) 
+(sum{j in bmltsIndex_RECTUM_185: (i,j) in voxelbmlt_RECTUM_185} x185[j]*intensities_RECTUM_185[i,j]) 
+(sum{j in bmltsIndex_RECTUM_230: (i,j) in voxelbmlt_RECTUM_230} x230[j]*intensities_RECTUM_230[i,j]) 
+(sum{j in bmltsIndex_RECTUM_310: (i,j) in voxelbmlt_RECTUM_310} x310[j]*intensities_RECTUM_310[i,j]) 
)^a[2])) )^(1/a[2])/EUD0[2])^v[2])^-1)- log((1+(( ((1/R_BLADDER)*(sum {i in voxelIndex_BLADDER} (
(sum{j in bmltsIndex_BLADDER_5: (i,j) in voxelbmlt_BLADDER_5} x5[j]*intensities_BLADDER_5[i,j]) 
+(sum{j in bmltsIndex_BLADDER_105: (i,j) in voxelbmlt_BLADDER_105} x105[j]*intensities_BLADDER_105[i,j]) 
+(sum{j in bmltsIndex_BLADDER_185: (i,j) in voxelbmlt_BLADDER_185} x185[j]*intensities_BLADDER_185[i,j]) 
+(sum{j in bmltsIndex_BLADDER_230: (i,j) in voxelbmlt_BLADDER_230} x230[j]*intensities_BLADDER_230[i,j]) 
+(sum{j in bmltsIndex_BLADDER_310: (i,j) in voxelbmlt_BLADDER_310} x310[j]*intensities_BLADDER_310[i,j]) 
)^a[3])) )^(1/a[3])/EUD0[3])^v[3])^-1);
 s.t. 
equalityTarget: 	( ((1/R_PTVHD)*(sum {i in voxelIndex_PTVHD}( 
(sum {j in bmltsIndex_PTVHD_5: (i,j) in voxelbmlt_PTVHD_5} x5[j]*intensities_PTVHD_5[i,j]) 
+(sum {j in bmltsIndex_PTVHD_105: (i,j) in voxelbmlt_PTVHD_105} x105[j]*intensities_PTVHD_105[i,j]) 
+(sum {j in bmltsIndex_PTVHD_185: (i,j) in voxelbmlt_PTVHD_185} x185[j]*intensities_PTVHD_185[i,j]) 
+(sum {j in bmltsIndex_PTVHD_230: (i,j) in voxelbmlt_PTVHD_230} x230[j]*intensities_PTVHD_230[i,j]) 
+(sum {j in bmltsIndex_PTVHD_310: (i,j) in voxelbmlt_PTVHD_310} x310[j]*intensities_PTVHD_310[i,j]) 
)^a[1])))^(1/a[1]) >= t; 
constraintOAR_Target: 	(((1/R_PTVHD)*(sum {i in voxelIndex_PTVHD} ( 
 (sum {j in bmltsIndex_PTVHD_5: (i,j) in voxelbmlt_PTVHD_5} x5[j]*intensities_PTVHD_5[i,j]) 
+(sum {j in bmltsIndex_PTVHD_105: (i,j) in voxelbmlt_PTVHD_105} x105[j]*intensities_PTVHD_105[i,j]) 
+(sum {j in bmltsIndex_PTVHD_185: (i,j) in voxelbmlt_PTVHD_185} x185[j]*intensities_PTVHD_185[i,j]) 
+(sum {j in bmltsIndex_PTVHD_230: (i,j) in voxelbmlt_PTVHD_230} x230[j]*intensities_PTVHD_230[i,j]) 
+(sum {j in bmltsIndex_PTVHD_310: (i,j) in voxelbmlt_PTVHD_310} x310[j]*intensities_PTVHD_310[i,j]) 
)^a[4])))^(1/a[4]) <=OAR_targetUB;
