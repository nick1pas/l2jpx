# Formerly based on Elektra's script
# updated by DrLecter for the Official L2J Datapack Project.
# Visit http://forum.l2jdp.com/ for more details.

qn = "3995_echo"

#print "3995. Echo"

NPCS        = [31042,31043]
ADENA       = 57
COST        = 200

QuestId     = 3995
QuestName   = "echo"
QuestDesc   = "custom"

#score:[crystal,msg_ok,msg_noadena,msg_noscore]
LIST={
4410:[4411,"01","02","03"],
4409:[4412,"04","05","06"],
4408:[4413,"07","08","09"],
4420:[4414,"10","11","12"],
4421:[4415,"13","14","15"],
4419:[4417,"16","05","06"],
4418:[4416,"17","05","06"]
}


import sys
from net.l2jpx.gameserver.model.quest import State
from net.l2jpx.gameserver.model.quest import QuestState
from net.l2jpx.gameserver.model.quest.jython import QuestJython as JQuest

class Quest (JQuest) :

 def __init__(self,id,name,descr): JQuest.__init__(self,id,name,descr)

 def onAdvEvent (self,event,npc,player) :
    st = player.getQuestState(qn)
    if not (st and event.isdigit()): return
    score=int(event)
    if score in LIST.keys() :
      crystal,ok,noadena,noscore=LIST[score]
      npcId = str(npc.getNpcId())
      if not st.getQuestItemsCount(score) :
         htmltext=npcId+"-"+noscore+".htm"
      elif st.getQuestItemsCount(ADENA) < COST :
         htmltext=npcId+"-"+noadena+".htm"
      else :
         st.takeItems(ADENA,COST)
         st.giveItems(crystal,1)
         htmltext=npcId+"-"+ok+".htm"
    else :
      htmltext=""
    st.exitQuest(1)
    return htmltext

 def onTalk (Self,npc,player):
   return "1.htm"


QUEST       = Quest(QuestId,qn,QuestDesc)
CREATED     = State('Start',     QUEST)

QUEST.setInitialState(CREATED)

for npc in NPCS:
   QUEST.addStartNpc(npc)
   QUEST.addTalkId(npc)