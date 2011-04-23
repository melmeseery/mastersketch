%%%%%%%%%get weight for all the database... 



face=double(imread('./average database/face.jpg'));

load('./average database/fets1');
%% test all 
correct=0;
persons=50;
samples=3; 

NumSamples=0;
%DataVector=zeros();
k=1;
  DataVector=[];
for i=1:persons 
for s=1:samples
    im=imread(strcat('./color dbase1/',num2str(i),'.',num2str(s),'.jpg'));
   % index=testFaceImage(im);
    
      [test_face_fets,InIm]=getFaceEigen(im,face,eigen_faces,i);
      
      DataVector=[ DataVector; InIm'];
      
      
end 
    
 
end 
 
save Data DataVector 
