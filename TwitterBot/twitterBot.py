#Cameron Burdsall
#COEN 166 Twitter Bot

import markovify, pandas, PIL, textwrap, twitter, tweepy

def uploadTweet (file, tweet):
    #procedure that uploads a tweet using the tweepy package
    #file = the image to be posted
    #tweet = text to be posted
    
    #set keys and tokens
    consumerKey = 'Y5TmLr4zZ2jpPnlsSHSP0MXZi'
    consumerSecret = 'YtYUZHE6R0H8HjhjJIKT5voBCjHQck1M3zPYFvN0edPeqUeAcO'
    accessToken = '1266481169161269248-H2dAjabIlgtHYeWIGAlltz3L2QpcTM'
    accessTokenSecret = 'M9nC4IGIp2VAyw16mrKPIgJKWvSD9xKBulGV56fuGHty0'

    #set authorization and access token
    auth = tweepy.OAuthHandler(consumerKey, consumerSecret)
    auth.set_access_token(accessToken, accessTokenSecret)
    #init api
    api = tweepy.API(auth)
    #upload file and set status
    api.update_with_media(file, status = tweet)

    print ('Tweet Successfully Posted')


from PIL import Image, ImageDraw, ImageFont

# ------- seed random values -------
from random import seed
from random import randint
from datetime import datetime
seed(datetime.now())

num = randint(0,3)

# ------- open files -------
imageFile = "C:/Users/Cameron Burdsall/Documents/Python/TwitterBot/pic"+str(num)+".png"
dataFile = "C:/Users/Cameron Burdsall/Documents/Python/TwitterBot/inspiration.txt"
tweetFile = 'C:/Users/Cameron Burdsall/Documents/Python/TwitterBot/tweet.png'

# ------- prepare image -------
image = Image.open(imageFile)
draw = ImageDraw.Draw(image)
#font settings
fontnum = randint(1,3)
fontFile = "C:/Users/Cameron Burdsall/Documents/Python/TwitterBot/font"+str(fontnum)+'.ttf'
font = ImageFont.truetype(fontFile, 24)
if (num >= 2):
    font = ImageFont.truetype(fontFile, 24*4)
(x,y) = (randint(30,50),randint(50,100))
color = 'rgb(255, 255, 255)'

with open (dataFile) as f:
    file = f.read()

# ------- generate text model using markovify -------
text_model = markovify.Text(file, state_size = 2)


# ------- generate tweet -------

#ensures a different text is generated each time
textnum = randint(1,1000)
for i in range (0, textnum):
    #need 280 character limit to fit in tweet
    message = text_model.make_short_sentence(280)
while (message == None):
    #in case a sentence is not generated and None is returned
    message = text_model.make_short_sentence(280)

print ('Tweet Generated')

#process tweet into multiple lines
lines = textwrap.wrap(message, width = 25)
height = 30
if (num >= 2):
    height = 80
#place tweet on image
i = 0
for line in lines:
    draw.text((x,y+height*i), line, fill=color, font=font)
    i = i + 1

#save tweet image
image.save(tweetFile)
print ('Image Generated, check ' + tweetFile)

#upload
uploadTweet(tweetFile, message)
