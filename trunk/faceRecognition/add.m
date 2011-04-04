function add(database_directory,persons_number,person_samples)
added_dir = input('Input the directory     ','s');
added_number = input('Input number of persons you want to add     ');
for i=1:added_number
    for j=0:person_samples
        copyfile(strcat(added_dir,num2str(i),'.',num2str(j),'.jpg'),strcat(database_directory,num2str(i+persons_number),'.',num2str(j),'.jpg'));
    end
end
addpreparing(database_directory,persons_number,added_number,person_samples)
