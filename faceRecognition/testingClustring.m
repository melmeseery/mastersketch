%%% test clusters 
load GlobalBest 
face=double(imread('./average database/face.jpg'));
load('./average database/fets1');

%% test all 
correct=0;
persons=50;
samples=3; 
     figure ;
NumSamples=0;
for i=1:persons
    
for s=1:samples
    im=imread(strcat('./color dbase1/',num2str(i),'.',num2str(s),'.jpg'));
    index=testFaceImageCluster(im,face,eigen_faces,globalBest)
    NumSamples=NumSamples+1;c
    if (index==i)
        correct=correct+1;
    else 
       % figure ;
       
        subplot (2,1,1) ;
        im=imread(strcat('./color dbase1/',num2str(i),'.',num2str(s),'.jpg'));
        imshow(im);
       
        im2=imread(strcat('./color dbase1/',num2str(index),'.',num2str(1),'.jpg'));
       subplot (2,1,2);
       imshow(im2);
       drawnow
    end 
    
    
end
end 
correct
NumSamples
result = (correct/NumSamples)*100
