function y=writeTrainSet(filename,FeatureSize,SamplesCounts,Y, labelPerSample,classesLabels)

numFeat=FeatureSize
trainCounts=SamplesCounts
fid= fopen([filename],'wt');

%%%%
%%% @RELATION    TrainStrokeTrainF20.txt.arff
fprintf(fid,'@RELATION    %s  \n \n',filename);
for i=1:FeatureSize 
%% @attribute   'F_0'  numeric  
fprintf(fid,'@attribute   ''F_%d''  numeric  \n',i);
end
fprintf(fid,'@attribute ''class''   {');
%%%@attribute 'class'   {1,2,3,4,5,6,7,8,9,10}
for j=1:length(classesLabels)
    if (j>1)
         fprintf(fid,',');
    end 
 fprintf(fid,'%d',classesLabels(j));
 end 
fprintf(fid,'}\n \n');
fprintf(fid,'@data  \n');

%fprintf(fid,'%d %d \n',trainCounts,numFeat+1);

 format=[repmat('%d, ',1,numFeat) ' %d \n'];
 sizeOFy=size(Y)
 %silbaels=size(labels)
 silabelPerSample=size(labelPerSample)
matFile=[ Y  labelPerSample']';
fprintf(fid,format,matFile);
fclose(fid);
disp('Finish writing the file');
y=1