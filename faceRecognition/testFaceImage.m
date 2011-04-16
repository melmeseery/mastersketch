function index=testFaceImage(im)

face=double(imread('./average database/face.jpg'));

load('./average database/fets1');
%indexTest=26;
%sample=1;
%accurecy=0;

wi=80;
hi=60;


test_face=im;
test_face=rgb2gray(test_face);
%imshow(test_face);
    test_face=double(test_face);
    test_face_avg=test_face - face;
   % imshow(test_face_avg)
    test_face_avg=reshape(test_face_avg,wi*hi,1);
  diff = 0; 
  InIm = [];
for i=1:size(eigen_faces,2)
  %  test_face=imread(strcat('d:\heba\facerecog\new\color dbase1\',num2str(i),'.',num2str(3),'.jpg'));
  %  test_face_fets=eigen_faces(:,i)'*test_face_avg;
    test_face_fets=dot(eigen_faces(:,i)',test_face_avg');
    InIm = [InIm;test_face_fets];
end;

% Find Euclidean distance
e=[];
for i=1:size(face_fets,2)
q = face_fets(:,i);
%DiffWeight = InIm-q;
%mag = norm(DiffWeight);
mag=distance_measure(InIm,q,1)
e = [e mag];
end

kk = 1:size(e,2);
%subplot(1,2,2);
%stem(kk,e);
%title('Eucledian distance of input image','fontsize',14);

MaximumValue=max(e);  % maximum eucledian distance
[MinimumValue,index]=min(e);    % minimum eucledian distance

end 
