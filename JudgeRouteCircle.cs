using System;
using System.Collections.Generic;
using System.Text;

namespace Algorithms
{
    class JudgeRouteCircle
    {
        public bool JudgeCircle(string moves)
        {
            int x = 0;
            int y = 0;
            for(int n = 0; n < moves.Length; n++)
            {
                if (moves[n] == 'U')
                {
                    y++;
                    continue;
                }
                if (moves[n] == 'D')
                {
                    y--;
                    continue;
                }

                if (moves[n] == 'R')
                {
                    x++;
                    continue;
                }
                if (moves[n] == 'L')
                    x--;               
            }
            return x == 0 && y == 0;
        }
    }
}
