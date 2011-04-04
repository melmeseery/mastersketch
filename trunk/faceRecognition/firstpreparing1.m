function firstpreparing(database_directory,persons_number,person_samples)
mkdir('d:\heba\new\color dbase1')
for i=1:persons_number
    for j=0:person_samples
    face=imread('d:\heba\facerecog\my database\40.2.jpg');
        %  face = imread(strcat(database_directory,num2str(i),'.',num2str(j),'.jpg'));
    face_size = size(face);
    imshow(face);
    face1 = face;
        for k=1:face_size(1) 
            for l=1:face_size(2)
                if (face(k,l,3) >=95 & face(k,l,1) >=100 & face(k,l,2) >=90);
                    face1(k,l,1) =255;
                    face1(k,l,2) =255;
                    face1(k,l,3) =255;
                end
            end
        end 
      
        se = strel('square',8);
        face1 = imerode(face1,se);
        imshow (face1)
   bw_face = im2bw(face1);             
   face_size = size(bw_face);
  
    cut = 1;
        for  k = 1: face_size(1)
            find_black = find(bw_face(k,:) == 0);
            if length(find_black) <= 0
        %        face(k,1:find_black(1)-1,:)=255;
         %       face(k,find_black(length(find_black))+1:face_size(2),:)=255;
                face(k,1:face_size(2),:)=255;
                cut = k + 1;
            end
        end
        face = imcrop(face,[1 cut face_size(2)-1 face_size(1)-cut]);
        face1 = imcrop(face1,[1 cut face_size(2)-1 face_size(1)-cut]);
        bw_face = imcrop(bw_face,[1 cut face_size(2)-1 face_size(1)-cut]);
 %       imshow (bw_face);
        %%%%%%%%%%%%%%%%
        face_size  = size(bw_face);
        face_hist=zeros(face_size(2));
        for ii=1:face_size(2)
            for jj=1:face_size(1)
                if bw_face(jj,ii)== 0
                    face_hist(ii)=face_hist(ii) + 1;
                end
            end
        end
       %%%%%%%%%%%%%%%%%%%%%
        iii=1;
        jjj=face_size(2);
        while iii < face_size(2)- 20
            if ((face_hist(iii+20)-face_hist(iii))>= 100 & face_hist(iii)~=0)
                cut(1) = iii;
                face_hist(cut(1));
               iii=face_size(2);
            else
                cut(1) = 40;
            end
            iii=iii+20;
        end
        cut(2) = 0;
        while jjj > face_hist(cut(1))
            if ((face_hist(jjj-40)-face_hist(jjj))>= 50 & face_hist(jjj)~=0)
                cut(2) = (jjj);
                jjj=0;
           end
            jjj=jjj-40;
        end
        if (cut(2)== cut(1))
            cut(2) = face_size(2)- 40;
        end
        
        %%%%%%%%%%%%%%%
        face = imcrop(face,[cut(1) 1 cut(2)-cut(1) face_size(1)-1]);
        bw_face = imcrop(bw_face,[cut(1) 1 cut(2)-cut(1) face_size(1)-1]);
        face1 = imcrop(face1,[cut(1) 1 cut(2)-cut(1) face_size(1)-1]);
        imshow(face1);
        face = imresize(face,[80 60],'bilinear');
        bw_face = imresize(bw_face,[80 60],'bilinear');
        face1 = imresize(face1,[80 60],'bilinear');
        
        se = strel('disk',5);
        bw_face = imerode(bw_face,se);
        face_size = size(bw_face);
      
        for k=1:face_size(1) 
            for l=1:face_size(2)
                if (bw_face(k,l) == 1);
                    face(k,l,1) =255;
                    face(k,l,2) =255;
                    face(k,l,3) =255;
                else 
                face(k,l,1) =face1(k,l,1);
                face(k,l,2) = face1(k,l,2);
               face(k,l,3) = face1(k,l,3);    
         
                end
            end
      end
       
        imshow(face);
     %   face = imresize(face,[80 60],'bilinear');
        imwrite(face,strcat('d:\heba\new\color dbase1\',num2str(i),'.',num2str(j),'.jpg'));
    end
end
