function  [test_face_fets,InIm]=getFaceEigen(im,face,eigen_faces,i)

wi=80;
hi=60;


test_face=im;
test_face=rgb2gray(test_face);

    test_face=double(test_face);
    test_face_avg=test_face - face;

    test_face_avg=reshape(test_face_avg,wi*hi,1);
  diff = 0; 
  InIm = [];
 for j=1:size(eigen_faces,2)
    test_face_fets=dot(eigen_faces(:,j)',test_face_avg');
    
        InIm = [InIm;test_face_fets];
end;

     test_face_fets=dot(eigen_faces(:,i)',test_face_avg');
 
 