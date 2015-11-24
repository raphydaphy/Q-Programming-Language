# Install vim and maven if it is not already downloaded
apt-get install vim maven

# Create the directories we need for the .vim files
mkdir $HOME/.vim $HOME/.vim/syntax $HOME/.vim/ftdetect

# Create and enter a directory for downloading the files
mkdir $HOME/src
cd $HOME/src

# Download the .vim files for the setup
wget https://raw.githubusercontent.com/raphydaphy/Q-Programming-Language/master/dev/ftdetect.vim https://raw.githubusercontent.com/raphydaphy/Q-Programming-Language/master/dev/syntax.vim

# Move the .vim files into the correct directories
cd $HOME
mv src/ftdetect.vim	.vim/ftdetect/QPL.vim
mv src/syntax.vim	.vim/syntax/QPL.vim
