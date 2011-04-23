function  index=testFaceImageCluster(im,face,eigen_faces,gbest)
type=1;
par=gbest.particle;
%% first get face eigen 
 [test_face_fets,dd]=getFaceEigen(im,face,eigen_faces,1);
 
 [rows csize]=size(par);
 
 for i=1:csize
 %Squared Euclidean distance  between centers and points
 %dis(i)=distance_measure( p.particle(i) , face_fets(:,j),type);
 dis(i)=distance_measure( par(:,i),dd,type);
 end
  %% assign to cluster 
[value c]=min(dis) ;
 
 index=c; 