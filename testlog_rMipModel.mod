option solver knitro; 
options knitro_options "wantsol=8 	 mip_selectdir=1 	 outlev=1 	 hessopt=6 	 strat_warm_start=1 relax=1"; 
param R_PTVHD;
set voxelIndex_PTVHD;
set bmltsIndex_PTVHD_20 ;
set voxelbmlt_PTVHD_20 within {voxelIndex_PTVHD,bmltsIndex_PTVHD_20};
param intensities_PTVHD_20 {voxelbmlt_PTVHD_20} >=0;
set bmltsIndex_PTVHD_70 ;
set voxelbmlt_PTVHD_70 within {voxelIndex_PTVHD,bmltsIndex_PTVHD_70};
param intensities_PTVHD_70 {voxelbmlt_PTVHD_70} >=0;
set bmltsIndex_PTVHD_95 ;
set voxelbmlt_PTVHD_95 within {voxelIndex_PTVHD,bmltsIndex_PTVHD_95};
param intensities_PTVHD_95 {voxelbmlt_PTVHD_95} >=0;
set bmltsIndex_PTVHD_100 ;
set voxelbmlt_PTVHD_100 within {voxelIndex_PTVHD,bmltsIndex_PTVHD_100};
param intensities_PTVHD_100 {voxelbmlt_PTVHD_100} >=0;
set bmltsIndex_PTVHD_105 ;
set voxelbmlt_PTVHD_105 within {voxelIndex_PTVHD,bmltsIndex_PTVHD_105};
param intensities_PTVHD_105 {voxelbmlt_PTVHD_105} >=0;
set bmltsIndex_PTVHD_110 ;
set voxelbmlt_PTVHD_110 within {voxelIndex_PTVHD,bmltsIndex_PTVHD_110};
param intensities_PTVHD_110 {voxelbmlt_PTVHD_110} >=0;
set bmltsIndex_PTVHD_120 ;
set voxelbmlt_PTVHD_120 within {voxelIndex_PTVHD,bmltsIndex_PTVHD_120};
param intensities_PTVHD_120 {voxelbmlt_PTVHD_120} >=0;
set bmltsIndex_PTVHD_125 ;
set voxelbmlt_PTVHD_125 within {voxelIndex_PTVHD,bmltsIndex_PTVHD_125};
param intensities_PTVHD_125 {voxelbmlt_PTVHD_125} >=0;
set bmltsIndex_PTVHD_130 ;
set voxelbmlt_PTVHD_130 within {voxelIndex_PTVHD,bmltsIndex_PTVHD_130};
param intensities_PTVHD_130 {voxelbmlt_PTVHD_130} >=0;
set bmltsIndex_PTVHD_140 ;
set voxelbmlt_PTVHD_140 within {voxelIndex_PTVHD,bmltsIndex_PTVHD_140};
param intensities_PTVHD_140 {voxelbmlt_PTVHD_140} >=0;
set bmltsIndex_PTVHD_155 ;
set voxelbmlt_PTVHD_155 within {voxelIndex_PTVHD,bmltsIndex_PTVHD_155};
param intensities_PTVHD_155 {voxelbmlt_PTVHD_155} >=0;
set bmltsIndex_PTVHD_165 ;
set voxelbmlt_PTVHD_165 within {voxelIndex_PTVHD,bmltsIndex_PTVHD_165};
param intensities_PTVHD_165 {voxelbmlt_PTVHD_165} >=0;
set bmltsIndex_PTVHD_175 ;
set voxelbmlt_PTVHD_175 within {voxelIndex_PTVHD,bmltsIndex_PTVHD_175};
param intensities_PTVHD_175 {voxelbmlt_PTVHD_175} >=0;
set bmltsIndex_PTVHD_185 ;
set voxelbmlt_PTVHD_185 within {voxelIndex_PTVHD,bmltsIndex_PTVHD_185};
param intensities_PTVHD_185 {voxelbmlt_PTVHD_185} >=0;
set bmltsIndex_PTVHD_200 ;
set voxelbmlt_PTVHD_200 within {voxelIndex_PTVHD,bmltsIndex_PTVHD_200};
param intensities_PTVHD_200 {voxelbmlt_PTVHD_200} >=0;
set bmltsIndex_PTVHD_205 ;
set voxelbmlt_PTVHD_205 within {voxelIndex_PTVHD,bmltsIndex_PTVHD_205};
param intensities_PTVHD_205 {voxelbmlt_PTVHD_205} >=0;
set bmltsIndex_PTVHD_220 ;
set voxelbmlt_PTVHD_220 within {voxelIndex_PTVHD,bmltsIndex_PTVHD_220};
param intensities_PTVHD_220 {voxelbmlt_PTVHD_220} >=0;
set bmltsIndex_PTVHD_225 ;
set voxelbmlt_PTVHD_225 within {voxelIndex_PTVHD,bmltsIndex_PTVHD_225};
param intensities_PTVHD_225 {voxelbmlt_PTVHD_225} >=0;
set bmltsIndex_PTVHD_230 ;
set voxelbmlt_PTVHD_230 within {voxelIndex_PTVHD,bmltsIndex_PTVHD_230};
param intensities_PTVHD_230 {voxelbmlt_PTVHD_230} >=0;
set bmltsIndex_PTVHD_240 ;
set voxelbmlt_PTVHD_240 within {voxelIndex_PTVHD,bmltsIndex_PTVHD_240};
param intensities_PTVHD_240 {voxelbmlt_PTVHD_240} >=0;
set bmltsIndex_PTVHD_250 ;
set voxelbmlt_PTVHD_250 within {voxelIndex_PTVHD,bmltsIndex_PTVHD_250};
param intensities_PTVHD_250 {voxelbmlt_PTVHD_250} >=0;
set bmltsIndex_PTVHD_255 ;
set voxelbmlt_PTVHD_255 within {voxelIndex_PTVHD,bmltsIndex_PTVHD_255};
param intensities_PTVHD_255 {voxelbmlt_PTVHD_255} >=0;
set bmltsIndex_PTVHD_260 ;
set voxelbmlt_PTVHD_260 within {voxelIndex_PTVHD,bmltsIndex_PTVHD_260};
param intensities_PTVHD_260 {voxelbmlt_PTVHD_260} >=0;
set bmltsIndex_PTVHD_280 ;
set voxelbmlt_PTVHD_280 within {voxelIndex_PTVHD,bmltsIndex_PTVHD_280};
param intensities_PTVHD_280 {voxelbmlt_PTVHD_280} >=0;
set bmltsIndex_PTVHD_285 ;
set voxelbmlt_PTVHD_285 within {voxelIndex_PTVHD,bmltsIndex_PTVHD_285};
param intensities_PTVHD_285 {voxelbmlt_PTVHD_285} >=0;
set bmltsIndex_PTVHD_305 ;
set voxelbmlt_PTVHD_305 within {voxelIndex_PTVHD,bmltsIndex_PTVHD_305};
param intensities_PTVHD_305 {voxelbmlt_PTVHD_305} >=0;
set bmltsIndex_PTVHD_310 ;
set voxelbmlt_PTVHD_310 within {voxelIndex_PTVHD,bmltsIndex_PTVHD_310};
param intensities_PTVHD_310 {voxelbmlt_PTVHD_310} >=0;
param R_RECTUM;
set voxelIndex_RECTUM;
set bmltsIndex_RECTUM_20 ;
set voxelbmlt_RECTUM_20 within {voxelIndex_RECTUM,bmltsIndex_RECTUM_20};
param intensities_RECTUM_20 {voxelbmlt_RECTUM_20} >=0;
set bmltsIndex_RECTUM_70 ;
set voxelbmlt_RECTUM_70 within {voxelIndex_RECTUM,bmltsIndex_RECTUM_70};
param intensities_RECTUM_70 {voxelbmlt_RECTUM_70} >=0;
set bmltsIndex_RECTUM_95 ;
set voxelbmlt_RECTUM_95 within {voxelIndex_RECTUM,bmltsIndex_RECTUM_95};
param intensities_RECTUM_95 {voxelbmlt_RECTUM_95} >=0;
set bmltsIndex_RECTUM_100 ;
set voxelbmlt_RECTUM_100 within {voxelIndex_RECTUM,bmltsIndex_RECTUM_100};
param intensities_RECTUM_100 {voxelbmlt_RECTUM_100} >=0;
set bmltsIndex_RECTUM_105 ;
set voxelbmlt_RECTUM_105 within {voxelIndex_RECTUM,bmltsIndex_RECTUM_105};
param intensities_RECTUM_105 {voxelbmlt_RECTUM_105} >=0;
set bmltsIndex_RECTUM_110 ;
set voxelbmlt_RECTUM_110 within {voxelIndex_RECTUM,bmltsIndex_RECTUM_110};
param intensities_RECTUM_110 {voxelbmlt_RECTUM_110} >=0;
set bmltsIndex_RECTUM_120 ;
set voxelbmlt_RECTUM_120 within {voxelIndex_RECTUM,bmltsIndex_RECTUM_120};
param intensities_RECTUM_120 {voxelbmlt_RECTUM_120} >=0;
set bmltsIndex_RECTUM_125 ;
set voxelbmlt_RECTUM_125 within {voxelIndex_RECTUM,bmltsIndex_RECTUM_125};
param intensities_RECTUM_125 {voxelbmlt_RECTUM_125} >=0;
set bmltsIndex_RECTUM_130 ;
set voxelbmlt_RECTUM_130 within {voxelIndex_RECTUM,bmltsIndex_RECTUM_130};
param intensities_RECTUM_130 {voxelbmlt_RECTUM_130} >=0;
set bmltsIndex_RECTUM_140 ;
set voxelbmlt_RECTUM_140 within {voxelIndex_RECTUM,bmltsIndex_RECTUM_140};
param intensities_RECTUM_140 {voxelbmlt_RECTUM_140} >=0;
set bmltsIndex_RECTUM_155 ;
set voxelbmlt_RECTUM_155 within {voxelIndex_RECTUM,bmltsIndex_RECTUM_155};
param intensities_RECTUM_155 {voxelbmlt_RECTUM_155} >=0;
set bmltsIndex_RECTUM_165 ;
set voxelbmlt_RECTUM_165 within {voxelIndex_RECTUM,bmltsIndex_RECTUM_165};
param intensities_RECTUM_165 {voxelbmlt_RECTUM_165} >=0;
set bmltsIndex_RECTUM_175 ;
set voxelbmlt_RECTUM_175 within {voxelIndex_RECTUM,bmltsIndex_RECTUM_175};
param intensities_RECTUM_175 {voxelbmlt_RECTUM_175} >=0;
set bmltsIndex_RECTUM_185 ;
set voxelbmlt_RECTUM_185 within {voxelIndex_RECTUM,bmltsIndex_RECTUM_185};
param intensities_RECTUM_185 {voxelbmlt_RECTUM_185} >=0;
set bmltsIndex_RECTUM_200 ;
set voxelbmlt_RECTUM_200 within {voxelIndex_RECTUM,bmltsIndex_RECTUM_200};
param intensities_RECTUM_200 {voxelbmlt_RECTUM_200} >=0;
set bmltsIndex_RECTUM_205 ;
set voxelbmlt_RECTUM_205 within {voxelIndex_RECTUM,bmltsIndex_RECTUM_205};
param intensities_RECTUM_205 {voxelbmlt_RECTUM_205} >=0;
set bmltsIndex_RECTUM_220 ;
set voxelbmlt_RECTUM_220 within {voxelIndex_RECTUM,bmltsIndex_RECTUM_220};
param intensities_RECTUM_220 {voxelbmlt_RECTUM_220} >=0;
set bmltsIndex_RECTUM_225 ;
set voxelbmlt_RECTUM_225 within {voxelIndex_RECTUM,bmltsIndex_RECTUM_225};
param intensities_RECTUM_225 {voxelbmlt_RECTUM_225} >=0;
set bmltsIndex_RECTUM_230 ;
set voxelbmlt_RECTUM_230 within {voxelIndex_RECTUM,bmltsIndex_RECTUM_230};
param intensities_RECTUM_230 {voxelbmlt_RECTUM_230} >=0;
set bmltsIndex_RECTUM_240 ;
set voxelbmlt_RECTUM_240 within {voxelIndex_RECTUM,bmltsIndex_RECTUM_240};
param intensities_RECTUM_240 {voxelbmlt_RECTUM_240} >=0;
set bmltsIndex_RECTUM_250 ;
set voxelbmlt_RECTUM_250 within {voxelIndex_RECTUM,bmltsIndex_RECTUM_250};
param intensities_RECTUM_250 {voxelbmlt_RECTUM_250} >=0;
set bmltsIndex_RECTUM_255 ;
set voxelbmlt_RECTUM_255 within {voxelIndex_RECTUM,bmltsIndex_RECTUM_255};
param intensities_RECTUM_255 {voxelbmlt_RECTUM_255} >=0;
set bmltsIndex_RECTUM_260 ;
set voxelbmlt_RECTUM_260 within {voxelIndex_RECTUM,bmltsIndex_RECTUM_260};
param intensities_RECTUM_260 {voxelbmlt_RECTUM_260} >=0;
set bmltsIndex_RECTUM_280 ;
set voxelbmlt_RECTUM_280 within {voxelIndex_RECTUM,bmltsIndex_RECTUM_280};
param intensities_RECTUM_280 {voxelbmlt_RECTUM_280} >=0;
set bmltsIndex_RECTUM_285 ;
set voxelbmlt_RECTUM_285 within {voxelIndex_RECTUM,bmltsIndex_RECTUM_285};
param intensities_RECTUM_285 {voxelbmlt_RECTUM_285} >=0;
set bmltsIndex_RECTUM_305 ;
set voxelbmlt_RECTUM_305 within {voxelIndex_RECTUM,bmltsIndex_RECTUM_305};
param intensities_RECTUM_305 {voxelbmlt_RECTUM_305} >=0;
set bmltsIndex_RECTUM_310 ;
set voxelbmlt_RECTUM_310 within {voxelIndex_RECTUM,bmltsIndex_RECTUM_310};
param intensities_RECTUM_310 {voxelbmlt_RECTUM_310} >=0;
param R_BLADDER;
set voxelIndex_BLADDER;
set bmltsIndex_BLADDER_20 ;
set voxelbmlt_BLADDER_20 within {voxelIndex_BLADDER,bmltsIndex_BLADDER_20};
param intensities_BLADDER_20 {voxelbmlt_BLADDER_20} >=0;
set bmltsIndex_BLADDER_70 ;
set voxelbmlt_BLADDER_70 within {voxelIndex_BLADDER,bmltsIndex_BLADDER_70};
param intensities_BLADDER_70 {voxelbmlt_BLADDER_70} >=0;
set bmltsIndex_BLADDER_95 ;
set voxelbmlt_BLADDER_95 within {voxelIndex_BLADDER,bmltsIndex_BLADDER_95};
param intensities_BLADDER_95 {voxelbmlt_BLADDER_95} >=0;
set bmltsIndex_BLADDER_100 ;
set voxelbmlt_BLADDER_100 within {voxelIndex_BLADDER,bmltsIndex_BLADDER_100};
param intensities_BLADDER_100 {voxelbmlt_BLADDER_100} >=0;
set bmltsIndex_BLADDER_105 ;
set voxelbmlt_BLADDER_105 within {voxelIndex_BLADDER,bmltsIndex_BLADDER_105};
param intensities_BLADDER_105 {voxelbmlt_BLADDER_105} >=0;
set bmltsIndex_BLADDER_110 ;
set voxelbmlt_BLADDER_110 within {voxelIndex_BLADDER,bmltsIndex_BLADDER_110};
param intensities_BLADDER_110 {voxelbmlt_BLADDER_110} >=0;
set bmltsIndex_BLADDER_120 ;
set voxelbmlt_BLADDER_120 within {voxelIndex_BLADDER,bmltsIndex_BLADDER_120};
param intensities_BLADDER_120 {voxelbmlt_BLADDER_120} >=0;
set bmltsIndex_BLADDER_125 ;
set voxelbmlt_BLADDER_125 within {voxelIndex_BLADDER,bmltsIndex_BLADDER_125};
param intensities_BLADDER_125 {voxelbmlt_BLADDER_125} >=0;
set bmltsIndex_BLADDER_130 ;
set voxelbmlt_BLADDER_130 within {voxelIndex_BLADDER,bmltsIndex_BLADDER_130};
param intensities_BLADDER_130 {voxelbmlt_BLADDER_130} >=0;
set bmltsIndex_BLADDER_140 ;
set voxelbmlt_BLADDER_140 within {voxelIndex_BLADDER,bmltsIndex_BLADDER_140};
param intensities_BLADDER_140 {voxelbmlt_BLADDER_140} >=0;
set bmltsIndex_BLADDER_155 ;
set voxelbmlt_BLADDER_155 within {voxelIndex_BLADDER,bmltsIndex_BLADDER_155};
param intensities_BLADDER_155 {voxelbmlt_BLADDER_155} >=0;
set bmltsIndex_BLADDER_165 ;
set voxelbmlt_BLADDER_165 within {voxelIndex_BLADDER,bmltsIndex_BLADDER_165};
param intensities_BLADDER_165 {voxelbmlt_BLADDER_165} >=0;
set bmltsIndex_BLADDER_175 ;
set voxelbmlt_BLADDER_175 within {voxelIndex_BLADDER,bmltsIndex_BLADDER_175};
param intensities_BLADDER_175 {voxelbmlt_BLADDER_175} >=0;
set bmltsIndex_BLADDER_185 ;
set voxelbmlt_BLADDER_185 within {voxelIndex_BLADDER,bmltsIndex_BLADDER_185};
param intensities_BLADDER_185 {voxelbmlt_BLADDER_185} >=0;
set bmltsIndex_BLADDER_200 ;
set voxelbmlt_BLADDER_200 within {voxelIndex_BLADDER,bmltsIndex_BLADDER_200};
param intensities_BLADDER_200 {voxelbmlt_BLADDER_200} >=0;
set bmltsIndex_BLADDER_205 ;
set voxelbmlt_BLADDER_205 within {voxelIndex_BLADDER,bmltsIndex_BLADDER_205};
param intensities_BLADDER_205 {voxelbmlt_BLADDER_205} >=0;
set bmltsIndex_BLADDER_220 ;
set voxelbmlt_BLADDER_220 within {voxelIndex_BLADDER,bmltsIndex_BLADDER_220};
param intensities_BLADDER_220 {voxelbmlt_BLADDER_220} >=0;
set bmltsIndex_BLADDER_225 ;
set voxelbmlt_BLADDER_225 within {voxelIndex_BLADDER,bmltsIndex_BLADDER_225};
param intensities_BLADDER_225 {voxelbmlt_BLADDER_225} >=0;
set bmltsIndex_BLADDER_230 ;
set voxelbmlt_BLADDER_230 within {voxelIndex_BLADDER,bmltsIndex_BLADDER_230};
param intensities_BLADDER_230 {voxelbmlt_BLADDER_230} >=0;
set bmltsIndex_BLADDER_240 ;
set voxelbmlt_BLADDER_240 within {voxelIndex_BLADDER,bmltsIndex_BLADDER_240};
param intensities_BLADDER_240 {voxelbmlt_BLADDER_240} >=0;
set bmltsIndex_BLADDER_250 ;
set voxelbmlt_BLADDER_250 within {voxelIndex_BLADDER,bmltsIndex_BLADDER_250};
param intensities_BLADDER_250 {voxelbmlt_BLADDER_250} >=0;
set bmltsIndex_BLADDER_255 ;
set voxelbmlt_BLADDER_255 within {voxelIndex_BLADDER,bmltsIndex_BLADDER_255};
param intensities_BLADDER_255 {voxelbmlt_BLADDER_255} >=0;
set bmltsIndex_BLADDER_260 ;
set voxelbmlt_BLADDER_260 within {voxelIndex_BLADDER,bmltsIndex_BLADDER_260};
param intensities_BLADDER_260 {voxelbmlt_BLADDER_260} >=0;
set bmltsIndex_BLADDER_280 ;
set voxelbmlt_BLADDER_280 within {voxelIndex_BLADDER,bmltsIndex_BLADDER_280};
param intensities_BLADDER_280 {voxelbmlt_BLADDER_280} >=0;
set bmltsIndex_BLADDER_285 ;
set voxelbmlt_BLADDER_285 within {voxelIndex_BLADDER,bmltsIndex_BLADDER_285};
param intensities_BLADDER_285 {voxelbmlt_BLADDER_285} >=0;
set bmltsIndex_BLADDER_305 ;
set voxelbmlt_BLADDER_305 within {voxelIndex_BLADDER,bmltsIndex_BLADDER_305};
param intensities_BLADDER_305 {voxelbmlt_BLADDER_305} >=0;
set bmltsIndex_BLADDER_310 ;
set voxelbmlt_BLADDER_310 within {voxelIndex_BLADDER,bmltsIndex_BLADDER_310};
param intensities_BLADDER_310 {voxelbmlt_BLADDER_310} >=0;
param bmlt20;
param bmlt70;
param bmlt95;
param bmlt100;
param bmlt105;
param bmlt110;
param bmlt120;
param bmlt125;
param bmlt130;
param bmlt140;
param bmlt155;
param bmlt165;
param bmlt175;
param bmlt185;
param bmlt200;
param bmlt205;
param bmlt220;
param bmlt225;
param bmlt230;
param bmlt240;
param bmlt250;
param bmlt255;
param bmlt260;
param bmlt280;
param bmlt285;
param bmlt305;
param bmlt310;
param totalbmlt; 	#number of beamlets 
param UB1;
param UB2;
param UB3;
param t;
param epsilon;
param OAR_targetUB;
var x20 {1 .. 69} >= 0, <=30.0, default 1; 
var x70 {1 .. 65} >= 0, <=30.0, default 1; 
var x95 {1 .. 59} >= 0, <=30.0, default 1; 
var x100 {1 .. 58} >= 0, <=30.0, default 1; 
var x105 {1 .. 63} >= 0, <=30.0, default 1; 
var x110 {1 .. 64} >= 0, <=30.0, default 1; 
var x120 {1 .. 64} >= 0, <=30.0, default 1; 
var x125 {1 .. 64} >= 0, <=30.0, default 1; 
var x130 {1 .. 63} >= 0, <=30.0, default 1; 
var x140 {1 .. 67} >= 0, <=30.0, default 1; 
var x155 {1 .. 69} >= 0, <=30.0, default 1; 
var x165 {1 .. 69} >= 0, <=30.0, default 1; 
var x175 {1 .. 72} >= 0, <=30.0, default 1; 
var x185 {1 .. 71} >= 0, <=30.0, default 1; 
var x200 {1 .. 71} >= 0, <=30.0, default 1; 
var x205 {1 .. 71} >= 0, <=30.0, default 1; 
var x220 {1 .. 70} >= 0, <=30.0, default 1; 
var x225 {1 .. 70} >= 0, <=30.0, default 1; 
var x230 {1 .. 69} >= 0, <=30.0, default 1; 
var x240 {1 .. 66} >= 0, <=30.0, default 1; 
var x250 {1 .. 65} >= 0, <=30.0, default 1; 
var x255 {1 .. 63} >= 0, <=30.0, default 1; 
var x260 {1 .. 61} >= 0, <=30.0, default 1; 
var x280 {1 .. 63} >= 0, <=30.0, default 1; 
var x285 {1 .. 64} >= 0, <=30.0, default 1; 
var x305 {1 .. 65} >= 0, <=30.0, default 1; 
var x310 {1 .. 67} >= 0, <=30.0, default 1; 
param a{1 .. 4}; 
param v{1 .. 4}; 
param EUD0{1 .. 4}; 
var intensityVoxel_PTVHD{1 .. 15172}; 
var intensityVoxel_RECTUM{1 .. 18128}; 
var intensityVoxel_BLADDER{1 .. 22936}; 

 param numBeams; #Total Number of Beams
param n; #Number of Selected Beams

 var beams{1 .. numBeams} binary;
param fix_b0;
param fix_b1;
param fix_b2;
param fix_b3;
minimize Total_Cost: - log((1+(( ((1/R_RECTUM)*(sum {i in voxelIndex_RECTUM} (
(sum{j in bmltsIndex_RECTUM_20: (i,j) in voxelbmlt_RECTUM_20} x20[j]*intensities_RECTUM_20[i,j]) 
+(sum{j in bmltsIndex_RECTUM_70: (i,j) in voxelbmlt_RECTUM_70} x70[j]*intensities_RECTUM_70[i,j]) 
+(sum{j in bmltsIndex_RECTUM_95: (i,j) in voxelbmlt_RECTUM_95} x95[j]*intensities_RECTUM_95[i,j]) 
+(sum{j in bmltsIndex_RECTUM_100: (i,j) in voxelbmlt_RECTUM_100} x100[j]*intensities_RECTUM_100[i,j]) 
+(sum{j in bmltsIndex_RECTUM_105: (i,j) in voxelbmlt_RECTUM_105} x105[j]*intensities_RECTUM_105[i,j]) 
+(sum{j in bmltsIndex_RECTUM_110: (i,j) in voxelbmlt_RECTUM_110} x110[j]*intensities_RECTUM_110[i,j]) 
+(sum{j in bmltsIndex_RECTUM_120: (i,j) in voxelbmlt_RECTUM_120} x120[j]*intensities_RECTUM_120[i,j]) 
+(sum{j in bmltsIndex_RECTUM_125: (i,j) in voxelbmlt_RECTUM_125} x125[j]*intensities_RECTUM_125[i,j]) 
+(sum{j in bmltsIndex_RECTUM_130: (i,j) in voxelbmlt_RECTUM_130} x130[j]*intensities_RECTUM_130[i,j]) 
+(sum{j in bmltsIndex_RECTUM_140: (i,j) in voxelbmlt_RECTUM_140} x140[j]*intensities_RECTUM_140[i,j]) 
+(sum{j in bmltsIndex_RECTUM_155: (i,j) in voxelbmlt_RECTUM_155} x155[j]*intensities_RECTUM_155[i,j]) 
+(sum{j in bmltsIndex_RECTUM_165: (i,j) in voxelbmlt_RECTUM_165} x165[j]*intensities_RECTUM_165[i,j]) 
+(sum{j in bmltsIndex_RECTUM_175: (i,j) in voxelbmlt_RECTUM_175} x175[j]*intensities_RECTUM_175[i,j]) 
+(sum{j in bmltsIndex_RECTUM_185: (i,j) in voxelbmlt_RECTUM_185} x185[j]*intensities_RECTUM_185[i,j]) 
+(sum{j in bmltsIndex_RECTUM_200: (i,j) in voxelbmlt_RECTUM_200} x200[j]*intensities_RECTUM_200[i,j]) 
+(sum{j in bmltsIndex_RECTUM_205: (i,j) in voxelbmlt_RECTUM_205} x205[j]*intensities_RECTUM_205[i,j]) 
+(sum{j in bmltsIndex_RECTUM_220: (i,j) in voxelbmlt_RECTUM_220} x220[j]*intensities_RECTUM_220[i,j]) 
+(sum{j in bmltsIndex_RECTUM_225: (i,j) in voxelbmlt_RECTUM_225} x225[j]*intensities_RECTUM_225[i,j]) 
+(sum{j in bmltsIndex_RECTUM_230: (i,j) in voxelbmlt_RECTUM_230} x230[j]*intensities_RECTUM_230[i,j]) 
+(sum{j in bmltsIndex_RECTUM_240: (i,j) in voxelbmlt_RECTUM_240} x240[j]*intensities_RECTUM_240[i,j]) 
+(sum{j in bmltsIndex_RECTUM_250: (i,j) in voxelbmlt_RECTUM_250} x250[j]*intensities_RECTUM_250[i,j]) 
+(sum{j in bmltsIndex_RECTUM_255: (i,j) in voxelbmlt_RECTUM_255} x255[j]*intensities_RECTUM_255[i,j]) 
+(sum{j in bmltsIndex_RECTUM_260: (i,j) in voxelbmlt_RECTUM_260} x260[j]*intensities_RECTUM_260[i,j]) 
+(sum{j in bmltsIndex_RECTUM_280: (i,j) in voxelbmlt_RECTUM_280} x280[j]*intensities_RECTUM_280[i,j]) 
+(sum{j in bmltsIndex_RECTUM_285: (i,j) in voxelbmlt_RECTUM_285} x285[j]*intensities_RECTUM_285[i,j]) 
+(sum{j in bmltsIndex_RECTUM_305: (i,j) in voxelbmlt_RECTUM_305} x305[j]*intensities_RECTUM_305[i,j]) 
+(sum{j in bmltsIndex_RECTUM_310: (i,j) in voxelbmlt_RECTUM_310} x310[j]*intensities_RECTUM_310[i,j]) 
)^a[2])) )^(1/a[2])/EUD0[2])^v[2])^-1)- log((1+(( ((1/R_BLADDER)*(sum {i in voxelIndex_BLADDER} (
(sum{j in bmltsIndex_BLADDER_20: (i,j) in voxelbmlt_BLADDER_20} x20[j]*intensities_BLADDER_20[i,j]) 
+(sum{j in bmltsIndex_BLADDER_70: (i,j) in voxelbmlt_BLADDER_70} x70[j]*intensities_BLADDER_70[i,j]) 
+(sum{j in bmltsIndex_BLADDER_95: (i,j) in voxelbmlt_BLADDER_95} x95[j]*intensities_BLADDER_95[i,j]) 
+(sum{j in bmltsIndex_BLADDER_100: (i,j) in voxelbmlt_BLADDER_100} x100[j]*intensities_BLADDER_100[i,j]) 
+(sum{j in bmltsIndex_BLADDER_105: (i,j) in voxelbmlt_BLADDER_105} x105[j]*intensities_BLADDER_105[i,j]) 
+(sum{j in bmltsIndex_BLADDER_110: (i,j) in voxelbmlt_BLADDER_110} x110[j]*intensities_BLADDER_110[i,j]) 
+(sum{j in bmltsIndex_BLADDER_120: (i,j) in voxelbmlt_BLADDER_120} x120[j]*intensities_BLADDER_120[i,j]) 
+(sum{j in bmltsIndex_BLADDER_125: (i,j) in voxelbmlt_BLADDER_125} x125[j]*intensities_BLADDER_125[i,j]) 
+(sum{j in bmltsIndex_BLADDER_130: (i,j) in voxelbmlt_BLADDER_130} x130[j]*intensities_BLADDER_130[i,j]) 
+(sum{j in bmltsIndex_BLADDER_140: (i,j) in voxelbmlt_BLADDER_140} x140[j]*intensities_BLADDER_140[i,j]) 
+(sum{j in bmltsIndex_BLADDER_155: (i,j) in voxelbmlt_BLADDER_155} x155[j]*intensities_BLADDER_155[i,j]) 
+(sum{j in bmltsIndex_BLADDER_165: (i,j) in voxelbmlt_BLADDER_165} x165[j]*intensities_BLADDER_165[i,j]) 
+(sum{j in bmltsIndex_BLADDER_175: (i,j) in voxelbmlt_BLADDER_175} x175[j]*intensities_BLADDER_175[i,j]) 
+(sum{j in bmltsIndex_BLADDER_185: (i,j) in voxelbmlt_BLADDER_185} x185[j]*intensities_BLADDER_185[i,j]) 
+(sum{j in bmltsIndex_BLADDER_200: (i,j) in voxelbmlt_BLADDER_200} x200[j]*intensities_BLADDER_200[i,j]) 
+(sum{j in bmltsIndex_BLADDER_205: (i,j) in voxelbmlt_BLADDER_205} x205[j]*intensities_BLADDER_205[i,j]) 
+(sum{j in bmltsIndex_BLADDER_220: (i,j) in voxelbmlt_BLADDER_220} x220[j]*intensities_BLADDER_220[i,j]) 
+(sum{j in bmltsIndex_BLADDER_225: (i,j) in voxelbmlt_BLADDER_225} x225[j]*intensities_BLADDER_225[i,j]) 
+(sum{j in bmltsIndex_BLADDER_230: (i,j) in voxelbmlt_BLADDER_230} x230[j]*intensities_BLADDER_230[i,j]) 
+(sum{j in bmltsIndex_BLADDER_240: (i,j) in voxelbmlt_BLADDER_240} x240[j]*intensities_BLADDER_240[i,j]) 
+(sum{j in bmltsIndex_BLADDER_250: (i,j) in voxelbmlt_BLADDER_250} x250[j]*intensities_BLADDER_250[i,j]) 
+(sum{j in bmltsIndex_BLADDER_255: (i,j) in voxelbmlt_BLADDER_255} x255[j]*intensities_BLADDER_255[i,j]) 
+(sum{j in bmltsIndex_BLADDER_260: (i,j) in voxelbmlt_BLADDER_260} x260[j]*intensities_BLADDER_260[i,j]) 
+(sum{j in bmltsIndex_BLADDER_280: (i,j) in voxelbmlt_BLADDER_280} x280[j]*intensities_BLADDER_280[i,j]) 
+(sum{j in bmltsIndex_BLADDER_285: (i,j) in voxelbmlt_BLADDER_285} x285[j]*intensities_BLADDER_285[i,j]) 
+(sum{j in bmltsIndex_BLADDER_305: (i,j) in voxelbmlt_BLADDER_305} x305[j]*intensities_BLADDER_305[i,j]) 
+(sum{j in bmltsIndex_BLADDER_310: (i,j) in voxelbmlt_BLADDER_310} x310[j]*intensities_BLADDER_310[i,j]) 
)^a[3])) )^(1/a[3])/EUD0[3])^v[3])^-1);
 s.t. 
maxNumBeams 				: 	sum {k in 1 .. numBeams} beams[k] 								= 	n; 
beamAvailable_x20{j in 1 .. 69}: x20[j] <= 	30*beams[1] ;
beamAvailable_x70{j in 1 .. 65}: x70[j] <= 	30*beams[2] ;
beamAvailable_x95{j in 1 .. 59}: x95[j] <= 	30*beams[3] ;
beamAvailable_x100{j in 1 .. 58}: x100[j] <= 	30*beams[4] ;
beamAvailable_x105{j in 1 .. 63}: x105[j] <= 	30*beams[5] ;
beamAvailable_x110{j in 1 .. 64}: x110[j] <= 	30*beams[6] ;
beamAvailable_x120{j in 1 .. 64}: x120[j] <= 	30*beams[7] ;
beamAvailable_x125{j in 1 .. 64}: x125[j] <= 	30*beams[8] ;
beamAvailable_x130{j in 1 .. 63}: x130[j] <= 	30*beams[9] ;
beamAvailable_x140{j in 1 .. 67}: x140[j] <= 	30*beams[10] ;
beamAvailable_x155{j in 1 .. 69}: x155[j] <= 	30*beams[11] ;
beamAvailable_x165{j in 1 .. 69}: x165[j] <= 	30*beams[12] ;
beamAvailable_x175{j in 1 .. 72}: x175[j] <= 	30*beams[13] ;
beamAvailable_x185{j in 1 .. 71}: x185[j] <= 	30*beams[14] ;
beamAvailable_x200{j in 1 .. 71}: x200[j] <= 	30*beams[15] ;
beamAvailable_x205{j in 1 .. 71}: x205[j] <= 	30*beams[16] ;
beamAvailable_x220{j in 1 .. 70}: x220[j] <= 	30*beams[17] ;
beamAvailable_x225{j in 1 .. 70}: x225[j] <= 	30*beams[18] ;
beamAvailable_x230{j in 1 .. 69}: x230[j] <= 	30*beams[19] ;
beamAvailable_x240{j in 1 .. 66}: x240[j] <= 	30*beams[20] ;
beamAvailable_x250{j in 1 .. 65}: x250[j] <= 	30*beams[21] ;
beamAvailable_x255{j in 1 .. 63}: x255[j] <= 	30*beams[22] ;
beamAvailable_x260{j in 1 .. 61}: x260[j] <= 	30*beams[23] ;
beamAvailable_x280{j in 1 .. 63}: x280[j] <= 	30*beams[24] ;
beamAvailable_x285{j in 1 .. 64}: x285[j] <= 	30*beams[25] ;
beamAvailable_x305{j in 1 .. 65}: x305[j] <= 	30*beams[26] ;
beamAvailable_x310{j in 1 .. 67}: x310[j] <= 	30*beams[27] ;
beam0: beams[fix_b0] = 1;
beam1: beams[fix_b1] = 1;
beam2: beams[fix_b2] = 1;
beam3: beams[fix_b3] = 1;
equalityTarget: 	( ((1/R_PTVHD)*(sum {i in voxelIndex_PTVHD}( 
(sum {j in bmltsIndex_PTVHD_20: (i,j) in voxelbmlt_PTVHD_20} x20[j]*intensities_PTVHD_20[i,j]) 
+(sum {j in bmltsIndex_PTVHD_70: (i,j) in voxelbmlt_PTVHD_70} x70[j]*intensities_PTVHD_70[i,j]) 
+(sum {j in bmltsIndex_PTVHD_95: (i,j) in voxelbmlt_PTVHD_95} x95[j]*intensities_PTVHD_95[i,j]) 
+(sum {j in bmltsIndex_PTVHD_100: (i,j) in voxelbmlt_PTVHD_100} x100[j]*intensities_PTVHD_100[i,j]) 
+(sum {j in bmltsIndex_PTVHD_105: (i,j) in voxelbmlt_PTVHD_105} x105[j]*intensities_PTVHD_105[i,j]) 
+(sum {j in bmltsIndex_PTVHD_110: (i,j) in voxelbmlt_PTVHD_110} x110[j]*intensities_PTVHD_110[i,j]) 
+(sum {j in bmltsIndex_PTVHD_120: (i,j) in voxelbmlt_PTVHD_120} x120[j]*intensities_PTVHD_120[i,j]) 
+(sum {j in bmltsIndex_PTVHD_125: (i,j) in voxelbmlt_PTVHD_125} x125[j]*intensities_PTVHD_125[i,j]) 
+(sum {j in bmltsIndex_PTVHD_130: (i,j) in voxelbmlt_PTVHD_130} x130[j]*intensities_PTVHD_130[i,j]) 
+(sum {j in bmltsIndex_PTVHD_140: (i,j) in voxelbmlt_PTVHD_140} x140[j]*intensities_PTVHD_140[i,j]) 
+(sum {j in bmltsIndex_PTVHD_155: (i,j) in voxelbmlt_PTVHD_155} x155[j]*intensities_PTVHD_155[i,j]) 
+(sum {j in bmltsIndex_PTVHD_165: (i,j) in voxelbmlt_PTVHD_165} x165[j]*intensities_PTVHD_165[i,j]) 
+(sum {j in bmltsIndex_PTVHD_175: (i,j) in voxelbmlt_PTVHD_175} x175[j]*intensities_PTVHD_175[i,j]) 
+(sum {j in bmltsIndex_PTVHD_185: (i,j) in voxelbmlt_PTVHD_185} x185[j]*intensities_PTVHD_185[i,j]) 
+(sum {j in bmltsIndex_PTVHD_200: (i,j) in voxelbmlt_PTVHD_200} x200[j]*intensities_PTVHD_200[i,j]) 
+(sum {j in bmltsIndex_PTVHD_205: (i,j) in voxelbmlt_PTVHD_205} x205[j]*intensities_PTVHD_205[i,j]) 
+(sum {j in bmltsIndex_PTVHD_220: (i,j) in voxelbmlt_PTVHD_220} x220[j]*intensities_PTVHD_220[i,j]) 
+(sum {j in bmltsIndex_PTVHD_225: (i,j) in voxelbmlt_PTVHD_225} x225[j]*intensities_PTVHD_225[i,j]) 
+(sum {j in bmltsIndex_PTVHD_230: (i,j) in voxelbmlt_PTVHD_230} x230[j]*intensities_PTVHD_230[i,j]) 
+(sum {j in bmltsIndex_PTVHD_240: (i,j) in voxelbmlt_PTVHD_240} x240[j]*intensities_PTVHD_240[i,j]) 
+(sum {j in bmltsIndex_PTVHD_250: (i,j) in voxelbmlt_PTVHD_250} x250[j]*intensities_PTVHD_250[i,j]) 
+(sum {j in bmltsIndex_PTVHD_255: (i,j) in voxelbmlt_PTVHD_255} x255[j]*intensities_PTVHD_255[i,j]) 
+(sum {j in bmltsIndex_PTVHD_260: (i,j) in voxelbmlt_PTVHD_260} x260[j]*intensities_PTVHD_260[i,j]) 
+(sum {j in bmltsIndex_PTVHD_280: (i,j) in voxelbmlt_PTVHD_280} x280[j]*intensities_PTVHD_280[i,j]) 
+(sum {j in bmltsIndex_PTVHD_285: (i,j) in voxelbmlt_PTVHD_285} x285[j]*intensities_PTVHD_285[i,j]) 
+(sum {j in bmltsIndex_PTVHD_305: (i,j) in voxelbmlt_PTVHD_305} x305[j]*intensities_PTVHD_305[i,j]) 
+(sum {j in bmltsIndex_PTVHD_310: (i,j) in voxelbmlt_PTVHD_310} x310[j]*intensities_PTVHD_310[i,j]) 
)^a[1])))^(1/a[1]) >= t; 
constraintOAR_Target: 	(((1/R_PTVHD)*(sum {i in voxelIndex_PTVHD} ( 
 (sum {j in bmltsIndex_PTVHD_20: (i,j) in voxelbmlt_PTVHD_20} x20[j]*intensities_PTVHD_20[i,j]) 
+(sum {j in bmltsIndex_PTVHD_70: (i,j) in voxelbmlt_PTVHD_70} x70[j]*intensities_PTVHD_70[i,j]) 
+(sum {j in bmltsIndex_PTVHD_95: (i,j) in voxelbmlt_PTVHD_95} x95[j]*intensities_PTVHD_95[i,j]) 
+(sum {j in bmltsIndex_PTVHD_100: (i,j) in voxelbmlt_PTVHD_100} x100[j]*intensities_PTVHD_100[i,j]) 
+(sum {j in bmltsIndex_PTVHD_105: (i,j) in voxelbmlt_PTVHD_105} x105[j]*intensities_PTVHD_105[i,j]) 
+(sum {j in bmltsIndex_PTVHD_110: (i,j) in voxelbmlt_PTVHD_110} x110[j]*intensities_PTVHD_110[i,j]) 
+(sum {j in bmltsIndex_PTVHD_120: (i,j) in voxelbmlt_PTVHD_120} x120[j]*intensities_PTVHD_120[i,j]) 
+(sum {j in bmltsIndex_PTVHD_125: (i,j) in voxelbmlt_PTVHD_125} x125[j]*intensities_PTVHD_125[i,j]) 
+(sum {j in bmltsIndex_PTVHD_130: (i,j) in voxelbmlt_PTVHD_130} x130[j]*intensities_PTVHD_130[i,j]) 
+(sum {j in bmltsIndex_PTVHD_140: (i,j) in voxelbmlt_PTVHD_140} x140[j]*intensities_PTVHD_140[i,j]) 
+(sum {j in bmltsIndex_PTVHD_155: (i,j) in voxelbmlt_PTVHD_155} x155[j]*intensities_PTVHD_155[i,j]) 
+(sum {j in bmltsIndex_PTVHD_165: (i,j) in voxelbmlt_PTVHD_165} x165[j]*intensities_PTVHD_165[i,j]) 
+(sum {j in bmltsIndex_PTVHD_175: (i,j) in voxelbmlt_PTVHD_175} x175[j]*intensities_PTVHD_175[i,j]) 
+(sum {j in bmltsIndex_PTVHD_185: (i,j) in voxelbmlt_PTVHD_185} x185[j]*intensities_PTVHD_185[i,j]) 
+(sum {j in bmltsIndex_PTVHD_200: (i,j) in voxelbmlt_PTVHD_200} x200[j]*intensities_PTVHD_200[i,j]) 
+(sum {j in bmltsIndex_PTVHD_205: (i,j) in voxelbmlt_PTVHD_205} x205[j]*intensities_PTVHD_205[i,j]) 
+(sum {j in bmltsIndex_PTVHD_220: (i,j) in voxelbmlt_PTVHD_220} x220[j]*intensities_PTVHD_220[i,j]) 
+(sum {j in bmltsIndex_PTVHD_225: (i,j) in voxelbmlt_PTVHD_225} x225[j]*intensities_PTVHD_225[i,j]) 
+(sum {j in bmltsIndex_PTVHD_230: (i,j) in voxelbmlt_PTVHD_230} x230[j]*intensities_PTVHD_230[i,j]) 
+(sum {j in bmltsIndex_PTVHD_240: (i,j) in voxelbmlt_PTVHD_240} x240[j]*intensities_PTVHD_240[i,j]) 
+(sum {j in bmltsIndex_PTVHD_250: (i,j) in voxelbmlt_PTVHD_250} x250[j]*intensities_PTVHD_250[i,j]) 
+(sum {j in bmltsIndex_PTVHD_255: (i,j) in voxelbmlt_PTVHD_255} x255[j]*intensities_PTVHD_255[i,j]) 
+(sum {j in bmltsIndex_PTVHD_260: (i,j) in voxelbmlt_PTVHD_260} x260[j]*intensities_PTVHD_260[i,j]) 
+(sum {j in bmltsIndex_PTVHD_280: (i,j) in voxelbmlt_PTVHD_280} x280[j]*intensities_PTVHD_280[i,j]) 
+(sum {j in bmltsIndex_PTVHD_285: (i,j) in voxelbmlt_PTVHD_285} x285[j]*intensities_PTVHD_285[i,j]) 
+(sum {j in bmltsIndex_PTVHD_305: (i,j) in voxelbmlt_PTVHD_305} x305[j]*intensities_PTVHD_305[i,j]) 
+(sum {j in bmltsIndex_PTVHD_310: (i,j) in voxelbmlt_PTVHD_310} x310[j]*intensities_PTVHD_310[i,j]) 
)^a[4])))^(1/a[4]) <=OAR_targetUB;
